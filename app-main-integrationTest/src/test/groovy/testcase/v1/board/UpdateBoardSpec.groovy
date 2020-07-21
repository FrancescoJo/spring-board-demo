/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardInfoResponse
import com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequest
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.generic.UnauthorisedException
import io.restassured.response.Response
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.endpoint.ApiPathsHelper
import test.endpoint.v1.board.dto.UpdateBoardRequestBuilder
import testcase.BoardTestBase

import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
class UpdateBoardSpec extends BoardTestBase {
    def "fail if not authenticated"() {
        given:
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final reqSpec = jsonRequestSpec("updateBoard-error-unauthorised", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPaths.BOARD)

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(401))).body

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if accessId is not a UUID format"() {
        given:
        final createdAuth = createRandomAuth()
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateBoard-error-illegalAccessId",
                createdAuth.accessToken.value,
                "__not-a-uuid-format__",
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if no user is created for authentication"() {
        given:
        final createdAuth = createRandomAuth()
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateBoard-error-noUserCreated",
                createdAuth.accessToken.value,
                UUID.randomUUID().toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == UserNotFoundException.class.simpleName
    }

    def "fail if board is not found for given accessId"() {
        given:
        final self = createRandomUser()
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateBoard-error-noBoardFound",
                self.accessToken,
                UUID.randomUUID().toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    @Unroll
    def "fail if board name('#name') is too short or too long"() {
        given:
        final self = createRandomUser()
        final board = createRandomBoardBy(self)
        final request = new UpdateBoardRequestBuilder(UpdateBoardRequestBuilder.createRandom())
                .name(name)
                .build()

        when:
        final reqSpec = sendRequest(
                "updateBoard-error-illegalNameFormat-#$docId",
                self.accessToken,
                board.accessId,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        name                  | docId
        ""                    | 1
        "1"                   | 2
        "1234567890123456789" | 3
    }

    def "only owner of board can update it"() {
        given:
        final self = createRandomUser()
        final notOwnedBoard = createRandomBoardBy(createRandomUser())
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateBoard-error-boardNotOwned",
                self.accessToken,
                notOwnedBoard.accessId,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(403))).body

        expect:
        errorBody.cause == UnauthorisedException.class.simpleName
    }

    def "board is updated if request is valid"() {
        given:
        final self = createRandomUser()
        final ownedBoard = createRandomBoardBy(self)
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateBoard",
                self.accessToken,
                ownedBoard.accessId,
                request,
                boardInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), BoardInfoResponse.class)

        expect:
        response.name == request.name
        response.description == request.description
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String accessId,
            final UpdateBoardRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .patch(ApiPathsHelper.BOARD_ACCESS_ID(accessId))
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("name")
                        .type(JsonFieldType.STRING)
                        .description(UpdateBoardRequest.DESC_NAME),
                fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description(UpdateBoardRequest.DESC_DESCRIPTION)
        )
    }
}
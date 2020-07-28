/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.request.UpdateBoardRequest
import com.github.fj.board.endpoint.v1.board.response.BoardInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.generic.UnauthorisedException
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequestBuilder
import testcase.v1.BoardTestBase

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
        final response = jsonRequestSpec("updateBoard-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPaths.BOARD)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if accessId is not a UUID format"() {
        given:
        final createdAuth = createRandomAuth()
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateBoard-error-illegalAccessId",
                createdAuth.accessToken.value,
                "__not-a-uuid-format__",
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if no user is created for authentication"() {
        given:
        final createdAuth = createRandomAuth()
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateBoard-error-noUserCreated",
                createdAuth.accessToken.value,
                UUID.randomUUID().toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, UserNotFoundException.STATUS)

        expect:
        errorBody.cause == UserNotFoundException.class.simpleName
    }

    def "fail if board is not found for given accessId"() {
        given:
        final self = createRandomUser()
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateBoard-error-noBoardFound",
                self.accessToken,
                UUID.randomUUID().toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    @Unroll
    def "fail if board name('#name') is too short or too long"() {
        given:
        final self = createRandomUser()
        final board = createRandomBoardOf(self)
        final request = new UpdateBoardRequestBuilder(UpdateBoardRequestBuilder.createRandom())
                .name(name)
                .build()

        when:
        final response = sendRequest(
                "updateBoard-error-illegalNameFormat-#$docId",
                self.accessToken,
                board.accessId.toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

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
        final notOwnedBoard = createRandomBoardOf(createRandomUser())
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateBoard-error-boardNotOwned",
                self.accessToken,
                notOwnedBoard.accessId.toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, UnauthorisedException.STATUS)

        expect:
        errorBody.cause == UnauthorisedException.class.simpleName
    }

    def "board is updated if request is valid"() {
        given:
        final self = createRandomUser()
        final ownedBoard = createRandomBoardOf(self)
        final request = UpdateBoardRequestBuilder.createRandom()

        when:
        final rawResponse = sendRequest(
                "updateBoard",
                self.accessToken,
                ownedBoard.accessId.toString(),
                request,
                boardInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoResponse.class)

        expect:
        response.name == request.name
        response.description == request.description
        response.access == request.access
        response.mode == request.mode
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
                .patch(ApiPathsHelper.BOARD_ID(accessId))
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("name")
                        .type(JsonFieldType.STRING)
                        .description(UpdateBoardRequest.DESC_NAME),
                fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description(UpdateBoardRequest.DESC_DESCRIPTION),
                fieldWithPath("access")
                        .type(JsonFieldType.STRING)
                        .description(UpdateBoardRequest.DESC_ACCESS),
                fieldWithPath("mode")
                        .type(JsonFieldType.STRING)
                        .description(UpdateBoardRequest.DESC_MODE)
        )
    }
}

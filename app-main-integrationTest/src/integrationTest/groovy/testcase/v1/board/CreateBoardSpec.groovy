/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.request.CreateBoardRequest
import com.github.fj.board.endpoint.v1.board.response.BoardInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.DuplicatedBoardKeyException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardStatus
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequestBuilder
import testcase.v1.BoardTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class CreateBoardSpec extends BoardTestBase {
    def "fail if not authenticated"() {
        given:
        final request = CreateBoardRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("createBoard-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .post(ApiPaths.BOARD)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if no user is created for authentication"() {
        given:
        final createdAuth = createRandomAuth()
        final request = CreateBoardRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "createBoard-error-noUserCreated",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, UserNotFoundException.STATUS)

        expect:
        errorBody.cause == UserNotFoundException.class.simpleName
    }

    @Unroll
    def "fail if board key('#key') is out of pattern"() {
        given:
        final self = createRandomUser()
        final request = new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .key(key)
                .build()

        when:
        final response = sendRequest(
                "createBoard-error-illegalKeyFormat-#$docId",
                self.accessToken,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        key                 | docId
        ""                  | 1
        "123"               | 2
        "12345678901234567" | 3
        "name and space"    | 4
        "Русский"           | 5
    }

    def "fail if board key is already exist"() {
        given:
        final createdBoard = createBoardBy(CreateBoardRequestBuilder.createRandom())
        final self = createRandomUser()
        final request = new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .key(createdBoard.key)
                .build()

        when:
        final response = sendRequest(
                "createBoard-error-duplicatedKey",
                self.accessToken,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, DuplicatedBoardKeyException.STATUS)

        expect:
        errorBody.cause == DuplicatedBoardKeyException.class.simpleName
    }

    @Unroll
    def "fail if board name('#name') is too short or too long"() {
        given:
        final self = createRandomUser()
        final request = new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .name(name)
                .build()

        when:
        final response = sendRequest(
                "createBoard-error-illegalNameFormat-#$docId",
                self.accessToken,
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

    def "board is created if request is valid"() {
        given:
        final self = createRandomUser()
        final request = CreateBoardRequestBuilder.createRandom()

        when:
        final rawResponse = sendRequest(
                "createBoard",
                self.accessToken,
                request,
                boardInfoResponseFieldsDoc()
        )

        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoResponse.class)

        then:
        !response.accessId.isEmpty()
        response.status == BoardStatus.NORMAL
        response.access == request.access
        response.mode == request.mode
        response.key == request.key
        response.name == request.name
        response.description == request.description
        response.postsCount == 0
        response.createdDate == response.modifiedDate
        response.creatorLoginName == self.loginName
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final CreateBoardRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .post(ApiPaths.BOARD)
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("key")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_KEY),
                fieldWithPath("name")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_NAME),
                fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_DESCRIPTION),
                fieldWithPath("access")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_ACCESS),
                fieldWithPath("mode")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_MODE)
        )
    }
}

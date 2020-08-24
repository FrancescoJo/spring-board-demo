/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.exception.client.reply.CannotCreateReplyException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.reply.dto.CreateReplyRequestBuilder
import testcase.v1.ReplyTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class CreateReplySpec extends ReplyTestBase {
    def "fail if not authenticated"() {
        given:
        final request = CreateReplyRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("createReply-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .post(currentRequestUrl())

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if parent post does not exist"() {
        given:
        final request = CreateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "createReply-error-noPostFound",
                requestUrl(UUID.randomUUID()),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, PostNotFoundException.STATUS)

        expect:
        errorBody.cause == PostNotFoundException.class.simpleName
    }

    def "fail with #expectedException if board is in #boardStatus state"() {
        given:
        updateBoardStatus(currentPost.boardId, boardStatus)
        final request = CreateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "createReply-error-illegalBoardStatus-#$docId",
                currentRequestUrl(),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, httpResponse)

        expect:
        errorBody.cause == expectedException

        where:
        boardStatus          | expectedException                     | httpResponse                      | docId
        BoardStatus.CLOSED   | BoardNotFoundException.simpleName     | BoardNotFoundException.STATUS     | 1
        BoardStatus.ARCHIVED | CannotCreateReplyException.simpleName | CannotCreateReplyException.STATUS | 2
    }

    def "fail if board is READ ONLY"() {
        given:
        updateBoardMode(self, currentBoard, BoardMode.READ_ONLY)
        final request = CreateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "createReply-error-readOnlyBoard",
                currentRequestUrl(),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotCreateReplyException.STATUS)

        expect:
        errorBody.cause == CannotCreateReplyException.class.simpleName
    }

    def "fail if reply is not allowed in post"() {
        given:
        updatePostMode(self, currentPost, PostMode.REPLY_NOT_ALLOWED)
        final request = CreateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "createReply-error-replyNotAllowed",
                currentRequestUrl(),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotCreateReplyException.STATUS)

        expect:
        errorBody.cause == CannotCreateReplyException.class.simpleName
    }

    def "fail if content is empty"() {
        given:
        final request = new CreateReplyRequestBuilder(CreateReplyRequestBuilder.createRandom())
                .content("")
                .build()

        when:
        final response = sendRequest(
                "createReply-error-emptyContent",
                currentRequestUrl(),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "reply is created if request is valid"() {
        given:
        final request = CreateReplyRequestBuilder.createRandom()

        when:
        final rawResponse = sendRequest(
                "createReply",
                currentRequestUrl(),
                request,
                requestFieldsDoc(),
                replyInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, ReplyInfoResponse.class)

        expect:
        response.writerNickname == self.nickname
        response.writerLoginName == self.loginName
        !response.edited
        response.number == 1
        response.contents == request.content
    }

    private String currentRequestUrl() {
        return requestUrl(currentPost.accessId)
    }

    private static String requestUrl(final UUID post) {
        return ApiPathsHelper.POST_ID_REPLY(post.toString())
    }

    private Response sendRequest(
            final String documentId,
            final String endPoint,
            final CreateReplyRequest request,
            final RequestFieldsSnippet reqDoc,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, reqDoc, respDoc)
                .body(request)
                .post(endPoint)
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("content")
                        .type(JsonFieldType.STRING)
                        .description(CreateReplyRequest.DESC_CONTENT)
        )
    }
}

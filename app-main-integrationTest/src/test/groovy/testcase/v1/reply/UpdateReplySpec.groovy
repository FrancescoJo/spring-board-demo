/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import com.github.fj.board.endpoint.v1.reply.request.UpdateReplyRequest
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.reply.CannotEditReplyException
import com.github.fj.board.exception.client.reply.ReplyNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.vo.reply.ReplyInfo
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.reply.dto.UpdateReplyRequestBuilder
import testcase.v1.ReplyTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Aug - 2020
 */
class UpdateReplySpec extends ReplyTestBase {
    private ReplyInfo currentReply

    def setup() {
        this.currentReply = createRandomReplyOf(self, currentPost)
    }

    def "fail if not authenticated"() {
        given:
        final request = UpdateReplyRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("updateReply-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(currentRequestUrl())

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if target reply is not found"() {
        given:
        final request = UpdateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateReply-error-noReplyFound",
                requestUrl(UUID.randomUUID()),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, ReplyNotFoundException.STATUS)

        expect:
        errorBody.cause == ReplyNotFoundException.class.simpleName
    }

    def "fail if target reply is not owned"() {
        given:
        final otherUserReply = createRandomReplyOf(createRandomUser(), currentPost)
        final request = UpdateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateReply-error-notOwned",
                requestUrl(otherUserReply.replyId),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotEditReplyException.STATUS)

        expect:
        errorBody.cause == CannotEditReplyException.class.simpleName
    }

    @Unroll
    def "fail if reply mode of parent post is #replyMode"() {
        given:
        updateCurrentPostMode(replyMode)
        final request = UpdateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateReply-error-illegalPostMode-#$docId",
                currentRequestUrl(),
                request,
                requestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotEditReplyException.STATUS)

        expect:
        errorBody.cause == CannotEditReplyException.class.simpleName

        where:
        replyMode                  | docId
        PostMode.REPLY_NOT_ALLOWED | 1
        PostMode.REPLY_ONCE        | 2
    }

    @Unroll
    def "fail with #expectedException if board has some constraints(#boardStatus, #boardMode)"() {
        given:
        updateCurrentBoardState(boardStatus, boardMode)
        final request = UpdateReplyRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateReply-error-illegalBoardMode-#$docId",
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
        boardStatus          | boardMode            | expectedException                   | httpResponse                    | docId
        BoardStatus.CLOSED   | BoardMode.FREE_STYLE | BoardNotFoundException.simpleName   | BoardNotFoundException.STATUS   | 1
        BoardStatus.ARCHIVED | BoardMode.FREE_STYLE | CannotEditReplyException.simpleName | CannotEditReplyException.STATUS | 2
        BoardStatus.NORMAL   | BoardMode.READ_ONLY  | CannotEditReplyException.simpleName | CannotEditReplyException.STATUS | 3
    }

    def "fail if content is empty"() {
        given:
        final request = new UpdateReplyRequestBuilder(UpdateReplyRequestBuilder.createRandom())
                .content("")
                .build()

        when:
        final response = sendRequest(
                "updateReply-error-emptyContent",
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

    def "reply is updated if request is valid"() {
        given:
        final request = UpdateReplyRequestBuilder.createRandom()

        when:
        final rawResponse = sendRequest(
                "updateReply",
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
        response.edited
        response.number == currentReply.number
        response.contents == request.content
    }

    private String currentRequestUrl() {
        return requestUrl(currentReply.replyId)
    }

    private static String requestUrl(final UUID replyId) {
        return ApiPathsHelper.REPLY_ID(replyId.toString())
    }

    private Response sendRequest(
            final String documentId,
            final String endPoint,
            final UpdateReplyRequest request,
            final RequestFieldsSnippet reqDoc,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, reqDoc, respDoc)
                .body(request)
                .patch(endPoint)
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("content")
                        .type(JsonFieldType.STRING)
                        .description(UpdateReplyRequest.DESC_CONTENT)
        )
    }
}

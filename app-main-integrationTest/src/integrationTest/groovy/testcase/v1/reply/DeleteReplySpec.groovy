/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.reply.CannotDeleteReplyException
import com.github.fj.board.exception.client.reply.ReplyNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.vo.reply.ReplyInfo
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Aug - 2020
 */
class DeleteReplySpec extends ReplyTestBase {
    private ReplyInfo currentReply

    def setup() {
        this.currentReply = createRandomReplyOf(self, currentPost)
    }

    def "fail if not authenticated"() {
        when:
        final response = jsonRequestSpec("deleteReply-error-unauthenticated", errorResponseFieldsDoc())
                .when()
                .delete(currentRequestUrl())

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if target reply is not found"() {
        when:
        final response = sendRequest(
                "deleteReply-error-noReplyFound",
                requestUrl(UUID.randomUUID()),
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

        when:
        final response = sendRequest(
                "deleteReply-error-notOwned",
                requestUrl(otherUserReply.replyId),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotDeleteReplyException.STATUS)

        expect:
        errorBody.cause == CannotDeleteReplyException.class.simpleName
    }

    @Unroll
    def "fail if reply mode of parent post is #replyMode"() {
        given:
        updateCurrentPostMode(replyMode)

        when:
        final response = sendRequest(
                "deleteReply-error-illegalPostMode-#$docId",
                currentRequestUrl(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotDeleteReplyException.STATUS)

        expect:
        errorBody.cause == CannotDeleteReplyException.class.simpleName

        where:
        replyMode                  | docId
        PostMode.REPLY_NOT_ALLOWED | 1
        PostMode.REPLY_ONCE        | 2
    }

    @Unroll
    def "fail with #expectedException if board has some constraints(#boardStatus, #boardMode)"() {
        given:
        updateCurrentBoardState(boardStatus, boardMode)

        when:
        final response = sendRequest(
                "deleteReply-error-illegalBoardMode-#$docId",
                currentRequestUrl(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, httpResponse)

        expect:
        errorBody.cause == expectedException

        where:
        boardStatus          | boardMode            | expectedException                     | httpResponse                      | docId
        BoardStatus.CLOSED   | BoardMode.FREE_STYLE | BoardNotFoundException.simpleName     | BoardNotFoundException.STATUS     | 1
        BoardStatus.ARCHIVED | BoardMode.FREE_STYLE | CannotDeleteReplyException.simpleName | CannotDeleteReplyException.STATUS | 2
        BoardStatus.NORMAL   | BoardMode.READ_ONLY  | CannotDeleteReplyException.simpleName | CannotDeleteReplyException.STATUS | 3
    }

    def "reply is deleted if request is valid"() {
        when:
        final rawResponse = sendRequest(
                "deleteReply",
                currentRequestUrl(),
                genericBooleanResponseDoc()
        )

        then:
        final response = expectGenericResponse(rawResponse, HttpStatus.OK, Boolean.class)

        expect: "response is true"
        response
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
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, respDoc)
                .delete(endPoint)
    }
}

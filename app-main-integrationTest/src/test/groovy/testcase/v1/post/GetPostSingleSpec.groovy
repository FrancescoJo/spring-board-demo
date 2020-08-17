/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.endpoint.v1.post.response.PostInfoDetailedResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Aug - 2020
 */
class GetPostSingleSpec extends ReplyTestBase {
    def "fail if postId is not in a UUID format"() {
        when:
        final response = unauthenticatedRequest(
                "getPostSingle-error-illegalAccessId",
                requestUrl("__not-a-uuid-format__"),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if target post is not found"() {
        when:
        final response = unauthenticatedRequest(
                "getPostSingle-error-noPostFound",
                requestUrl(UUID.randomUUID()),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, PostNotFoundException.STATUS)

        expect:
        errorBody.cause == PostNotFoundException.class.simpleName
    }

    @Unroll
    def "fail if target target board is inaccessible(#status, #access) for unauthenticated user"() {
        given:
        updateBoardAccess(currentBoard.accessId, access)
        updateBoardStatus(currentBoard.accessId, status)

        when:
        final response = unauthenticatedRequest(
                "getPostSingle-error-inaccessibleBoard-#$docId",
                currentRequestUrl(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.simpleName

        where:
        status             | access                   | docId
        BoardStatus.CLOSED | BoardAccess.PUBLIC       | 1
        BoardStatus.NORMAL | BoardAccess.MEMBERS_ONLY | 2
    }

    def "post detail and latest replies are returned"() {
        given:
        final replies = CollectionUtilsKt.iterationsOf(5) {
            createRandomReplyOf(createRandomUser(), currentPost)
        }

        when:
        final rawResponse = sendRequest(
                "getPostSingle",
                currentRequestUrl(),
                postInfoDetailedResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, PostInfoDetailedResponse.class)

        expect:
        response.boardId == currentBoard.accessId.toString()
        response.postId == currentPost.accessId.toString()
        !response.edited
        response.writerNickname == self.nickname
        response.writerLoginName == self.loginName
        response.title == currentPost.title
        response.contents == currentPost.contents
        response.attachments.size() == currentPost.attachments.size()
        response.replies.data.size() == replies.size()
        response.viewCount == currentPost.viewedCount + 1
    }

    private String currentRequestUrl() {
        return requestUrl(currentPost.accessId)
    }

    private static String requestUrl(final UUID postId) {
        return ApiPathsHelper.POST_ID(postId.toString())
    }

    private static String requestUrl(final String postId) {
        return ApiPathsHelper.POST_ID(postId)
    }

    private Response unauthenticatedRequest(
            final String documentId,
            final String endPoint,
            final ResponseFieldsSnippet respDoc
    ) {
        return jsonRequestSpec(documentId, respDoc)
                .when()
                .get(endPoint)
    }

    private Response sendRequest(
            final String documentId,
            final String endPoint,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, respDoc)
                .get(endPoint)
    }
}

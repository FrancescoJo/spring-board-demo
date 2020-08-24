/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

/**
 * Test fixtures for demonstrating basic requirements and fetching latest replies
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class GetLatestRepliesSpec extends ReplyTestBase {
    def "fail if parent post is not found"() {
        when:
        final response = jsonRequestSpec("getReplies-error-postNotFound", errorResponseFieldsDoc())
                .when()
                .get(requestUrl(UUID.randomUUID()))

        then:
        final errorBody = expectError(response, PostNotFoundException.STATUS)

        expect:
        errorBody.cause == PostNotFoundException.class.simpleName
    }

    def "fail if parent post UUID is in illegal format"() {
        when:
        final response = jsonRequestSpec("getReplies-error-illegalAccessId", errorResponseFieldsDoc())
                .when()
                .get(requestUrl("__not-a-uuid-format__"))

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if user is unauthenticated and parent post is in non-public board"() {
        given:
        updateBoardAccess(currentBoard.accessId, BoardAccess.MEMBERS_ONLY)

        when:
        final response = jsonRequestSpec("getReplies-error-boardIsMembersOnly", errorResponseFieldsDoc())
                .when()
                .get(currentRequestUrl())

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "only latest 20 replies are returned by default"() {
        given:
        final int replyCount = 21L
        final self = this.self
        final parentPost = this.currentPost
        final replies = CollectionUtilsKt.iterationsOf(replyCount) {
            createRandomReplyOf(self, parentPost)
        }
        final expected = replies.subList(1, replies.size())

        when:
        final rawResponse = sendRequest("getLatestReplies", pageableResponseDoc(replyInfoResponseFields("body.data[]")))

        then:
        final response = expectPageableResponse(rawResponse, HttpStatus.OK, ReplyInfoResponse.class)

        expect:
        response.totalCount == replyCount
        response.page == ContentsFetchCriteria.PAGE_DEFAULT
        response.size == GetRepliesService.DEFAULT_REPLY_FETCH_SIZE
        response.data.size() == expected.size()
        response.data.first().number == 21L
        response.data.last().number == 2L
    }

    private String currentRequestUrl() {
        return requestUrl(currentPost.accessId)
    }

    private static String requestUrl(final UUID postId) {
        return ApiPathsHelper.POST_ID_REPLIES(postId.toString())
    }

    private static String requestUrl(final String postId) {
        return ApiPathsHelper.POST_ID_REPLIES(postId)
    }

    private Response sendRequest(
            final String documentId,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, respDoc)
                .get(ApiPathsHelper.POST_ID_REPLIES(currentPost.accessId.toString()))
    }
}

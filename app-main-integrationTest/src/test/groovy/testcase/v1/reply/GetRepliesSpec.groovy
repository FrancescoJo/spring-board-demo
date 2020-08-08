/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.post.PostNotFoundException
import io.restassured.response.Response
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class GetRepliesSpec extends ReplyTestBase {
    def "fail if parent post is not found"() {
        when:
        final response = jsonRequestSpec("getReplies-error-postNotFound", errorResponseFieldsDoc())
                .when()
                .get(ApiPathsHelper.POST_ID_REPLIES(UUID.randomUUID().toString()))

        then:
        final errorBody = expectError(response, PostNotFoundException.STATUS)

        expect:
        errorBody.cause == PostNotFoundException.class.simpleName
    }

    def "fail if parent post UUID is in illegal format"() {
        when:
        final response = jsonRequestSpec("getReplies-error-illegalAccessId", errorResponseFieldsDoc())
                .when()
                .get(ApiPathsHelper.POST_ID_REPLIES("__not-a-uuid-format__"))

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

    }

    def "fail if user is unauthenticated and post is in non-public board"() {

    }

    def "only latest 20 replies are returned by default"() {

    }

    def "replies could be paged in certain amounts"() {

    }

    private Response sendRequest(
            final String documentId,
            final UUID postId,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, respDoc)
                .get(ApiPathsHelper.POST_ID_REPLIES(postId.toString()))
    }
}

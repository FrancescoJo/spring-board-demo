/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import io.restassured.response.Response
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

/**
 * Test fixtures for demonstrating how to use request parameters to achieve paging and endless fetching
 * from client side
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class GetRepliesSpec extends ReplyTestBase {
    def "empty list would be returned if an unexpected value is given for page"() {

    }

    @Unroll
    def "fetch size must be #fetchSize for #expected"() {

        // where:
        // fetchSize | expected
        // 0         | 20
        // 51        | 50
    }

    def "pagination is possible by returned offset and totalCount values"() {

    }

    private Response sendRequest(
            final String documentId,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, respDoc)
                .get(ApiPathsHelper.POST_ID_REPLIES(currentPost.accessId.toString()))
    }
}

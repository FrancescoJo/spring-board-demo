/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.vo.reply.ReplyInfo
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

import javax.annotation.Nullable

/**
 * Test fixtures for demonstrating how to use request parameters to achieve paging and endless fetching
 * from client side
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class GetRepliesSpec extends ReplyTestBase {
    def "empty list would be returned if an unexpected page number is given"() {
        given:
        final replyCount = 20
        createBulkReplies(replyCount)

        when:
        final rawResponse = sendRequest("getReplies-pageTooLarge",
                "${FetchCriteriaRequestMixin.GET_LIST_PARAM_PAGE}=${Integer.MAX_VALUE}",
                pageableResponseDoc()
        )

        then:
        final response = expectPageableResponse(rawResponse, HttpStatus.OK, ReplyInfoResponse.class)

        expect:
        response.totalCount == replyCount
        response.data.isEmpty()
    }

    @Unroll
    def "fetch size must be #expectedSize for requestedCount=#fetchSize"() {
        given:
        createBulkReplies(100)

        when:
        final rawResponse = sendRequest("getReplies-limitedFetchSize-#$docId",
                "${FetchCriteriaRequestMixin.GET_LIST_PARAM_COUNT}=$fetchSize",
                pageableResponseDoc(replyInfoResponseFields("body.data[]"))
        )

        then:
        final response = expectPageableResponse(rawResponse, HttpStatus.OK, ReplyInfoResponse.class)

        expect:
        response.data.size() == expectedSize

        where:
        fetchSize | expectedSize | docId
        0         | 20           | 1
        51        | 50           | 2
    }

    private List<ReplyInfo> createBulkReplies(final int iterations) {
        final user = self
        final post = currentPost

        return CollectionUtilsKt.iterationsOf(iterations) { createRandomReplyOf(user, post) }
    }

    private Response sendRequest(
            final String documentId,
            final @Nullable String query,
            final ResponseFieldsSnippet respDoc
    ) {
        final StringBuilder basePath = new StringBuilder(ApiPathsHelper.POST_ID_REPLIES(currentPost.accessId.toString()))
        if (query != null) {
            basePath.append("?$query")
        }

        return authenticatedRequest(documentId, self.accessToken, respDoc)
                .get(basePath.toString())
    }
}

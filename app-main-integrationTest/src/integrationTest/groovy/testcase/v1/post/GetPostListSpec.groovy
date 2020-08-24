/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.post.PostDetailedInfo
import com.github.fj.board.vo.reply.ReplyInfo
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.spockframework.util.Pair
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.v1.ReplyTestBase

import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveInt

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Aug - 2020
 */
class GetPostListSpec extends ReplyTestBase {
    def "fail if boardId is not in a UUID format"() {
        when:
        final response = unauthenticatedRequest(
                "getPostList-error-illegalAccessId",
                requestUrl("__not-a-uuid-format__"),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if target board is not found"() {
        when:
        final response = unauthenticatedRequest(
                "getPostList-error-noBoardFound",
                requestUrl(UUID.randomUUID()),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    @Unroll
    def "fail if target target board is inaccessible(#status, #access) for unauthenticated user"() {
        given:
        updateBoardAccess(currentBoard.accessId, access)
        updateBoardStatus(currentBoard.accessId, status)

        when:
        final response = unauthenticatedRequest(
                "getPostList-error-inaccessibleBoard-#$docId",
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

    def "latest post list is returned if no search criteria is given"() {
        given:
        final posts = CollectionUtilsKt.iterationsOf(21) {
            final post = createRandomPostOf(self, currentBoard)
            final repliesCount = getRandomPositiveInt(1, 6)
            final replies = CollectionUtilsKt.iterationsOf(repliesCount) { createRandomReplyOf(self, post) }

            return Pair.<PostDetailedInfo, List<ReplyInfo>> of(post, replies)
        }
        final expected = posts.subList(1, posts.size())

        when:
        final rawResponse = sendRequest(
                "getPostList-defaultLatest",
                currentRequestUrl(),
                pageableResponseDoc(postInfoBriefResponseFields("body.data[]"))
        )

        then:
        final response = expectPageableResponse(rawResponse, HttpStatus.OK, PostInfoBriefResponse.class)

        expect:
        response.page == ContentsFetchCriteria.PAGE_DEFAULT
        response.size == GetPostService.DEFAULT_POST_FETCH_SIZE
        response.data.size() == expected.size()
        response.data.first().postNumber == 22L
        response.data.last().postNumber == 3L
    }

    def "post list is returned for given criteria"() {
        given:
        final board = createRandomBoardOf(self)
        final postsCount = 21
        final targetPage = 2
        CollectionUtilsKt.iterationsOf(postsCount) {
            final post = createRandomPostOf(self, board)
            final repliesCount = getRandomPositiveInt(1, 6)
            final replies = CollectionUtilsKt.iterationsOf(repliesCount) { createRandomReplyOf(self, post) }

            return Pair.<PostDetailedInfo, List<ReplyInfo>> of(post, replies)
        }

        when:
        final rawResponse = sendRequest(
                "getPostList-defaultLatest-secondPage",
                requestUrl(board.accessId) + "?${FetchCriteriaRequestMixin.GET_LIST_PARAM_PAGE}=${targetPage}",
                pageableResponseDoc(postInfoBriefResponseFields("body.data[]"))
        )

        then:
        final response = expectPageableResponse(rawResponse, HttpStatus.OK, PostInfoBriefResponse.class)

        response.data.forEach {
            println(it)
        }

        expect:
        response.page == targetPage
        response.totalCount == postsCount
        response.size == GetPostService.DEFAULT_POST_FETCH_SIZE
        response.data.size() == 2
        response.data.first().postNumber == 2L
        response.data.last().postNumber == 1L
    }

    private String currentRequestUrl() {
        return requestUrl(currentBoard.accessId)
    }

    private static String requestUrl(final UUID boardId) {
        return ApiPathsHelper.BOARD_ID_POSTS(boardId.toString())
    }

    private static String requestUrl(final String boardId) {
        return ApiPathsHelper.BOARD_ID_POSTS(boardId)
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

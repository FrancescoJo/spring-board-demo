/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotDeletePostException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import testcase.common.CreatedUser
import testcase.v1.PostTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
class DeletePostSpec extends PostTestBase {
    private CreatedUser self
    private BoardInfo board
    private PostDetailedInfo post

    def setup() {
        this.self = createRandomUser()
        this.board = createRandomBoardOf(self)
        this.post = createRandomPostOf(self, board)
    }

    def "fail if not authenticated"() {
        when:
        final response = jsonRequestSpec("deletePost-error-unauthenticated", errorResponseFieldsDoc())
                .when()
                .delete(currentRequestUrl())

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    @Unroll
    def "fail with #expectedException if board is in #boardStatus state"() {
        given:
        updateBoardStatus(board.accessId, boardStatus)

        when:
        final response = sendRequest(
                "deletePost-error-illegalBoardStatus-#$docId",
                currentRequestUrl(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, httpResponse)

        expect:
        errorBody.cause == expectedException

        where:
        boardStatus          | expectedException                    | httpResponse                     | docId
        BoardStatus.CLOSED   | BoardNotFoundException.simpleName    | BoardNotFoundException.STATUS    | 1
        BoardStatus.ARCHIVED | CannotDeletePostException.simpleName | CannotDeletePostException.STATUS | 2
    }

    def "fail if board for given boardId is in READ ONLY mode"() {
        given:
        updateBoardMode(self, board, BoardMode.READ_ONLY)

        when:
        final response = sendRequest(
                "deletePost-error-readOnlyBoard",
                currentRequestUrl(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotDeletePostException.STATUS)

        expect:
        errorBody.cause == CannotDeletePostException.class.simpleName
    }

    def "fail if target post is not found"() {
        when:
        final response = sendRequest(
                "deletePost-error-noPostFound",
                requestUrl(UUID.randomUUID()),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, PostNotFoundException.STATUS)

        expect:
        errorBody.cause == PostNotFoundException.class.simpleName
    }

    def "fail if target post is not owned"() {
        given:
        final otherUserPost = createRandomPostOf(createRandomUser(), board)

        when:
        final response = sendRequest(
                "deletePost-error-otherUserPost",
                requestUrl(otherUserPost.accessId),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotDeletePostException.STATUS)

        expect:
        errorBody.cause == CannotDeletePostException.class.simpleName

    }

    def "post is deleted if request is valid"() {
        when:
        final rawResponse = sendRequest(
                "deletePost",
                currentRequestUrl(),
                genericBooleanResponseDoc()
        )

        then:
        final response = expectGenericResponse(rawResponse, HttpStatus.OK, Boolean.class)

        expect: "response is true"
        response
    }

    private String currentRequestUrl() {
        return requestUrl(post.accessId)
    }

    private static String requestUrl(final UUID post) {
        return ApiPathsHelper.POST_ID(post.toString())
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

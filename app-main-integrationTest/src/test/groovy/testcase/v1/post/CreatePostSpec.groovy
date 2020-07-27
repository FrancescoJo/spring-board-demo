/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.exception.generic.UnauthenticatedException
import test.com.github.fj.board.endpoint.v1.post.dto.CreatePostRequestBuilder
import testcase.v1.PostTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class CreatePostSpec extends PostTestBase {
    def "fail if not authenticated"() {
        given:
        final request = CreatePostRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("createPost-error-unauthenticated", createRequestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .post(ApiPaths.BOARD)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if board for given boardId is not present"() {

    }

    def "fail if board for given boardId is not in NORMAL state"() {

    }

    def "fail if board for given boardId is in READ ONLY mode"() {

    }

    def "success if request is valid"() {

    }

    def "consecutive posting increases post number in board"() {

    }
}

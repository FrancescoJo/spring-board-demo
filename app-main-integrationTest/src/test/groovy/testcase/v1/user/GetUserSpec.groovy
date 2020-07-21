/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.user

import com.github.fj.board.endpoint.v1.user.dto.UserInfoResponse
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import io.restassured.response.Response
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.ApiPathsHelper
import testcase.UserTestBase

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString
import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
class GetUserSpec extends UserTestBase {
    def "fail if not authenticated"() {
        when:
        final reqSpec = jsonRequestSpec("getUser-error-unauthenticated", errorResponseFieldsDoc())
                .when()
                .get(ApiPathsHelper.USER_NICKNAME(getRandomAlphaNumericString(8)))

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(401))).body

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if user with nickname is not found"() {
        given:
        final createdAuth = createRandomAuth()

        when:
        final reqSpec = sendRequest(
                "getUser-error-nicknameNotFound",
                createdAuth.accessToken.value,
                getRandomAlphaNumericString(8),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == UserNotFoundException.class.simpleName
    }

    def "detailed information is returned if user found with given nickname"() {
        given:
        final myAuth = createRandomAuth()
        final createdUser = createRandomUser()

        when:
        final reqSpec = sendRequest(
                "getUser",
                myAuth.accessToken.value,
                createdUser.nickname,
                userInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), UserInfoResponse.class)

        expect:
        response.nickname == createdUser.nickname
        response.status == createdUser.status
        response.email == createdUser.email
        response.createdDate == createdUser.createdDate
        response.lastActiveDate == createdUser.lastActiveDate
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String targetNickname,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, respDoc)
                .get(ApiPathsHelper.USER_NICKNAME(targetNickname))
    }
}

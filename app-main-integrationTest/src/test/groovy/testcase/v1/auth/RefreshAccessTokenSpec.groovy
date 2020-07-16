/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.component.security.HttpAuthorizationToken
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.RefreshTokenRequest
import com.github.fj.board.exception.client.RefreshTokenMismatchException
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.vo.auth.UserAgent
import io.restassured.http.Header
import testcase.AuthTestBase

import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class RefreshAccessTokenSpec extends AuthTestBase {
    def "fail if refreshToken mismatches"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(createdAuth.refreshToken)

        when:
        final reqSpec = jsonRequestSpec()
                .when()
                .header(new Header(HttpAuthorizationToken.HEADER_NAME, "Token ${createdAuth.accessToken.value}"))
                .header(new Header(UserAgent.HEADER_NAME, "${PlatformType.WEB.userAgentName}; "))
                .body(request)
                .patch(ApiPaths.TOKEN)

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(403))).body

        expect:
        errorBody.cause == RefreshTokenMismatchException.class.simpleName
    }

    // "fail if given refreshToken is too old": RefreshTokenMismatchException

    // "success if refreshToken is matched"
}

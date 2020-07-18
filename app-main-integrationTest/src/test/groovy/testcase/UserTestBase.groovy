/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.dto.CreateUserResponse
import test.endpoint.v1.user.dto.CreateUserRequestBuilder

import java.time.LocalDateTime

import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
class UserTestBase extends AuthTestBase {
    protected final CreatedUser createRandomUser() {
        final createdAuth = createRandomAuth()
        final request = CreateUserRequestBuilder.createRandom()

        final reqSpec = authenticatedRequest(createdAuth.accessToken.value)
                .body(request)
                .post(ApiPaths.USER)

        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), CreateUserResponse.class)

        return new CreatedUser(
                /*loginName */                 createdAuth.loginName,
                /*accessToken */               createdAuth.accessToken.value,
                /* accessTokenExpiresAfter */  createdAuth.accessTokenExpiresAfter,
                /* refreshToken */             createdAuth.refreshToken.value,
                /* refreshTokenExpiresAfter */ createdAuth.refreshTokenExpiresAfter,
                /* nickname */                 response.nickname,
                /* email */                    response.email
        )
    }

    static class CreatedUser {
        final String loginName
        final String accessToken
        final LocalDateTime accessTokenExpiresAfter
        final String refreshToken
        final LocalDateTime refreshTokenExpiresAfter
        final String nickname
        final String email

        CreatedUser(
                final String loginName,
                final String accessToken,
                final LocalDateTime accessTokenExpiresAfter,
                final String refreshToken,
                final LocalDateTime refreshTokenExpiresAfter,
                final String nickname,
                final String email
        ) {
            this.loginName = loginName
            this.accessToken = accessToken
            this.accessTokenExpiresAfter = accessTokenExpiresAfter
            this.refreshToken = refreshToken
            this.refreshTokenExpiresAfter = refreshTokenExpiresAfter
            this.nickname = nickname
            this.email = email
        }

        @Override
        String toString() {
            return "CreatedUser{" +
                    "loginName='$loginName'" +
                    ", accessToken='$accessToken'" +
                    ", accessTokenExpiresAfter=$accessTokenExpiresAfter" +
                    ", refreshToken='$refreshToken" +
                    ", refreshTokenExpiresAfter=$refreshTokenExpiresAfter" +
                    ", nickname='$nickname'" +
                    ", email='$email'" +
                    '}'
        }
    }
}

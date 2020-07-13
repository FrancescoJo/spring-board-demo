/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.endpoint.v1.auth.dto.SignUpResponse
import test.endpoint.v1.auth.dto.SignUpRequestBuilder

import static org.hamcrest.CoreMatchers.is

/**
 * A template class for auth related integration tests.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 13 - Jul - 2020
 */
class AuthTestBase extends IntegrationTestBase {
    protected final SignUpResponse createRandomAuth() {
        return createAuthFor(SignUpRequestBuilder.createRandom())
    }

    protected final SignUpResponse createAuthFor(final SignUpRequest request) {
        final reqSpec = jsonRequestSpec()
                .when()
                .body(request)
                .post(ApiPaths.AUTH)

        return expectResponse(reqSpec.then().assertThat().statusCode(is(200)), SignUpResponse.class)
    }
}

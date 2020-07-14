/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationResponse
import com.github.fj.board.exception.client.DuplicatedLoginNameException
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.lib.time.DateTimeUtilsKt
import org.springframework.http.converter.HttpMessageNotReadableException
import spock.lang.Unroll
import test.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import testcase.AuthTestBase

import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpSpec extends AuthTestBase {
    def "empty request payload does nothing"() {
        when:
        final reqSpec = sendSignUpRequest("signUp-error-emptyRequest", getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == HttpMessageNotReadableException.class.simpleName
    }

    @Unroll
    def "loginName('#loginName') must be 4 to 16 alphanumeric characters long"() {
        given:
        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .loginName(loginName)
                .build()

        when:
        final reqSpec = sendSignUpRequest("signUp-error-wrongLoginName-#$docId", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        loginName              | docId
        "ad"                   | 1
        "asdfasdfasdfasdfadsf" | 2
        "n`o`n@lph@^um"        | 3
    }

    @Unroll
    def "password('#password') must be 40 characters long"() {
        given:
        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .password(password)
                .build()

        when:
        final reqSpec = sendSignUpRequest("signUp-error-wrongPassword-#$docId", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        password                                  | docId
        ""                                        | 1
        "123456789012345678901234567890123456789" | 2
    }

    def "unknown client platformType is not allowed"() {
        given:
        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .platformType(PlatformType.UNDEFINED)
                .build()

        when:
        final reqSpec = sendSignUpRequest("signUp-error-unknownClientPlatform", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "client app version must be formatted as SemanticVersion"() {
        given:
        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .appVersion("1234567890")
                .buildAsJsonBy(getJsonMapper())

        when:
        final reqSpec = sendSignUpRequest("signUp-error-wrongClientAppVersion", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "already occupied loginName cannot be used for signUp"() {
        given: "Preoccupy a login name"
        final request = AuthenticationRequestBuilder.createRandom()
        createAuthFor(request)

        when:
        final reqSpec = sendSignUpRequest("signup-error-duplicatedLoginName", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(409))).body

        expect:
        errorBody.cause == DuplicatedLoginNameException.class.simpleName
    }

    def "plain good sign-up request would be successful"() {
        given:
        final now = DateTimeUtilsKt.utcNow()
        final request = AuthenticationRequestBuilder.createRandom()

        when:
        final reqSpec = sendSignUpRequest("signUp", request, authResponseFieldsDoc())

        then:
        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), AuthenticationResponse.class)

        expect:
        response.loginName == request.loginName
        !response.accessToken.value.isEmpty()
        response.accessTokenExpiresAfter > now
        !response.refreshToken.value.isEmpty()
        response.refreshTokenExpiresAfter > now
        response.refreshTokenExpiresAfter > response.accessTokenExpiresAfter
    }
}

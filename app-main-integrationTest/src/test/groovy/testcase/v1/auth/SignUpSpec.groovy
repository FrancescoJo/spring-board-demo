/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.v1.auth.response.AuthenticationResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.auth.LoginNotAllowedException
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.lib.time.DateTimeUtilsKt
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import testcase.v1.AuthTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpSpec extends AuthTestBase {
    def "empty request payload does nothing"() {
        when:
        final response = sendSignUpRequest("signUp-error-emptyRequest", errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, HttpStatus.BAD_REQUEST)

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
        final response = sendSignUpRequest("signUp-error-wrongLoginName-#$docId", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

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
        final response = sendSignUpRequest("signUp-error-wrongPassword-#$docId", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

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
        final response = sendSignUpRequest("signUp-error-unknownClientPlatform", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "client app version must be formatted as SemanticVersion"() {
        given:
        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .appVersion("1234567890")
                .buildAsJsonBy(getJsonMapper())

        when:
        final response = sendSignUpRequest("signUp-error-wrongClientAppVersion", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "already occupied loginName cannot be used for signUp"() {
        given: "Preoccupy a login name"
        final request = AuthenticationRequestBuilder.createRandom()
        createAuthFor(request)

        when:
        final response = sendSignUpRequest("signup-error-duplicatedLoginName", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, LoginNotAllowedException.STATUS)

        expect:
        errorBody.cause == LoginNotAllowedException.class.simpleName
    }

    def "plain good sign-up request would be successful"() {
        given:
        final now = DateTimeUtilsKt.utcNow()
        final request = AuthenticationRequestBuilder.createRandom()

        when:
        final rawResponse = sendSignUpRequest("signUp", request, authResponseFieldsDoc())

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, AuthenticationResponse.class)

        expect:
        response.loginName == request.loginName
        !response.accessToken.value.isEmpty()
        response.accessTokenExpiresAfter >= now
        !response.refreshToken.value.isEmpty()
        response.refreshTokenExpiresAfter >= now
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.v1.auth.response.AuthenticationResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.auth.LoginNotAllowedException
import com.github.fj.board.persistence.model.auth.PlatformType
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import test.com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import testcase.v1.AuthTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Jul - 2020
 */
class SignInSpec extends AuthTestBase {
    def "empty request payload does nothing"() {
        when:
        final response = sendSignInRequest("signIn-error-emptyRequest", errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, HttpStatus.BAD_REQUEST)

        expect:
        errorBody.cause == HttpMessageNotReadableException.class.simpleName
    }

    def "fail if no matching loginName is found"() {
        given:
        final request = AuthenticationRequestBuilder.createRandom()

        when:
        final response = sendSignInRequest("signIn-error-loginNameNotFound", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, HttpStatus.BAD_REQUEST)

        expect:
        errorBody.cause == LoginNotAllowedException.class.simpleName
    }

    def "fail if wrong password is given"() {
        given: "Preoccupy a user"
        final preRequest = AuthenticationRequestBuilder.createRandom()
        createAuthFor(preRequest)

        and:
        final request = new AuthenticationRequestBuilder(preRequest)
                .passwordHashed("")
                .build()

        when:
        final response = sendSignInRequest("signIn-error-wrongPassword", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, LoginNotAllowedException.STATUS)

        expect:
        errorBody.cause == LoginNotAllowedException.class.simpleName
    }

    def "unknown client platformType is not allowed"() {
        given:
        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .platformType(PlatformType.UNDEFINED)
                .build()

        when:
        final response = sendSignInRequest("signIn-error-unknownClientPlatform", request, errorResponseFieldsDoc())

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
        final response = sendSignInRequest("signIn-error-wrongClientAppVersion", request, errorResponseFieldsDoc())

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "auth token is refreshed after successful login"() {
        given: "Preoccupy a user"
        final preRequest = AuthenticationRequestBuilder.createRandom()
        final preResponse = createAuthFor(preRequest)

        and:
        final request = new AuthenticationRequestBuilder(preRequest).build()

        when:
        final rawResponse = sendSignInRequest("signIn", request, authResponseFieldsDoc())

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, AuthenticationResponse.class)

        expect:
        response.loginName == preResponse.loginName
        response.accessToken.value != preResponse.accessToken.value
        response.accessTokenExpiresAfter >= preResponse.accessTokenExpiresAfter
        response.refreshToken.value !=  preResponse.refreshToken.value
        response.refreshTokenExpiresAfter >= preResponse.refreshTokenExpiresAfter
    }
}

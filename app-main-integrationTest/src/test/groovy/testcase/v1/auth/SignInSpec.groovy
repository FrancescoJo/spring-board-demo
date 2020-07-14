/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import testcase.AuthTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Jul - 2020
 */
class SignInSpec extends AuthTestBase {
    def "empty request payload does nothing"() {
//        when:
//        final reqSpec = sendSignInRequest("signIn-error-emptyRequest", getErrorResponseFields())
//
//        then:
//        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body
//
//        expect:
//        errorBody.cause == HttpMessageNotReadableException.class.simpleName
    }

    def "would fail if no matching loginName is found"() {
//        given:
//        final request = AuthenticationRequestBuilder.createRandom()
//
//        when:
//        final reqSpec = sendSignInRequest("signIn-error-loginNameNotFound", request, getErrorResponseFields())
//
//        then:
//        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body
//
//        expect:
//        errorBody.cause == LoginNotAllowedException.class.simpleName
    }

    def "would fail if wrong password is given"() {
//        given: "Preoccupy a user"
//        final preRequest = AuthenticationRequestBuilder.createRandom()
//        createAuthFor(preRequest)
//
//        and:
//        final request = new AuthenticationRequestBuilder(preRequest)
//            .password("")
//            .build()
//
//        when:
//        final reqSpec = sendSignInRequest("signIn-error-wrongPassword", request, getErrorResponseFields())
//
//        then:
//        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body
//
//        expect:
//        errorBody.cause == LoginNotAllowedException.class.simpleName
    }

    def "unknown client platformType is not allowed"() {
//        given:
//        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
//                .platformType(PlatformType.UNDEFINED)
//                .build()
//
//        when:
//        final reqSpec = sendSignInRequest("signIn-error-unknownClientPlatform", request, getErrorResponseFields())
//
//        then:
//        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body
//
//        expect:
//        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "client app version must be formatted as SemanticVersion"() {
//        given:
//        final request = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
//                .appVersion("1234567890")
//                .buildAsJsonBy(getJsonMapper())
//
//        when:
//        final reqSpec = sendSignInRequest("signIn-error-wrongClientAppVersion", request, getErrorResponseFields())
//
//        then:
//        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body
//
//        expect:
//        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "auth token is refreshed after successful login"() {
//        given: "Preoccupy a user"
//        final preRequest = AuthenticationRequestBuilder.createRandom()
//        final preResponse = createAuthFor(preRequest)
//
//        when:
//        final reqSpec = sendSignInRequest("signIn", request, getErrorResponseFields())
//
//        then:
//        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), AuthenticationResponse.class)
//
//        expect:
//        response.loginName == preResponse.loginName
//        response.accessToken.value != preResponse.accessToken.value
//        response.accessTokenExpiresAfter >  preResponse.accessTokenExpiresAfter
//        response.refreshToken.value !=  preResponse.refreshToken.value
//        response.refreshTokenExpiresAfter > preResponse.refreshTokenExpiresAfter
    }
}

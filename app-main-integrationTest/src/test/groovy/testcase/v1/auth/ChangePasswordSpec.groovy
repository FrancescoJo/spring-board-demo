/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.request.ChangePasswordRequest
import com.github.fj.board.endpoint.v1.auth.response.AuthenticationResponse
import com.github.fj.board.exception.client.auth.DuplicatedPasswordException
import com.github.fj.board.exception.client.auth.WrongPasswordException
import com.github.fj.board.exception.generic.UnauthenticatedException
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import test.com.github.fj.board.endpoint.v1.auth.dto.ChangePasswordRequestBuilder
import testcase.v1.AuthTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class ChangePasswordSpec extends AuthTestBase {
    def "fail if not authenticated"() {
        given:
        final request = ChangePasswordRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("changePassword-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPaths.PASSWORD)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if old password mismatches saved one"() {
        given:
        final oldPassword = "__PASSWORD__"
        final authReq = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .passwordHashed(oldPassword)
                .build()
        final createdAuth = createAuthFor(authReq)

        and:
        final request = ChangePasswordRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "changePassword-error-wrongPassword",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, WrongPasswordException.STATUS)

        expect:
        errorBody.cause == WrongPasswordException.class.simpleName
    }

    def "fail if password is not actually changed"() {
        given:
        final oldPassword = "__PASSWORD__"
        final authReq = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .passwordHashed(oldPassword)
                .build()
        final createdAuth = createAuthFor(authReq)

        and:
        final request = new ChangePasswordRequestBuilder(ChangePasswordRequestBuilder.createRandom())
                .oldPassword(oldPassword)
                .newPassword(oldPassword)
                .build()

        when:
        final response = sendRequest(
                "changePassword-error-duplicatedPassword",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, DuplicatedPasswordException.STATUS)

        expect:
        errorBody.cause == DuplicatedPasswordException.class.simpleName
    }

    def "success is new password is good, and token is renewed"() {
        given:
        final oldPassword = "__PASSWORD__"
        final authReq = new AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
                .passwordHashed(oldPassword)
                .build()
        final createdAuth = createAuthFor(authReq)

        and:
        final request = new ChangePasswordRequestBuilder(ChangePasswordRequestBuilder.createRandom())
                .oldPassword(oldPassword)
                .build()

        when:
        final rawResponse = sendRequest(
                "changePassword",
                createdAuth.accessToken.value,
                request,
                authResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, AuthenticationResponse.class)

        expect:
        response.loginName == createdAuth.loginName
        response.accessToken.value != createdAuth.accessToken.value
        response.accessTokenExpiresAfter >= createdAuth.accessTokenExpiresAfter
        response.refreshToken.value != createdAuth.refreshToken.value
        response.refreshTokenExpiresAfter >= createdAuth.refreshTokenExpiresAfter
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final ChangePasswordRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .patch(ApiPaths.PASSWORD)
    }

    static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("oldPassword")
                        .type(JsonFieldType.STRING)
                        .description(ChangePasswordRequest.DESC_OLD_PASSWORD),
                fieldWithPath("newPassword")
                        .type(JsonFieldType.STRING)
                        .description(ChangePasswordRequest.DESC_NEW_PASSWORD)
        )
    }
}

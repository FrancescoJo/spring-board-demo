/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationResponse
import com.github.fj.board.endpoint.v1.auth.dto.ChangePasswordRequest
import com.github.fj.board.exception.client.DuplicatedPasswordException
import com.github.fj.board.exception.client.WrongPasswordException
import com.github.fj.board.exception.generic.UnauthorisedException
import io.restassured.response.Response
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import test.endpoint.v1.auth.dto.ChangePasswordRequestBuilder
import testcase.AuthTestBase

import static org.hamcrest.CoreMatchers.is
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
        final reqSpec = jsonRequestSpec("changePassword-error-unauthorised", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPaths.PASSWORD)

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(401))).body

        expect:
        errorBody.cause == UnauthorisedException.class.simpleName
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
        final reqSpec = sendRequest(
                "changePassword-error-wrongPassword",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

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
        final reqSpec = sendRequest(
                "changePassword-error-duplicatedPassword",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

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
        final reqSpec = sendRequest(
                "changePassword",
                createdAuth.accessToken.value,
                request,
                authResponseFieldsDoc()
        )

        then:
        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), AuthenticationResponse.class)

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

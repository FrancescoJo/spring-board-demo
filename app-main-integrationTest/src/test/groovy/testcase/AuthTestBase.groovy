/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationResponse
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.v1.auth.dto.AuthenticationRequestBuilder

import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.*

/**
 * A template class for auth related integration tests.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 13 - Jul - 2020
 */
class AuthTestBase extends IntegrationTestBase {
    protected final AuthenticationResponse createRandomAuth() {
        return createAuthFor(AuthenticationRequestBuilder.createRandom())
    }

    protected final AuthenticationResponse createAuthFor(final AuthenticationRequest request) {
        final reqSpec = jsonRequestSpec()
                .when()
                .body(request)
                .post(ApiPaths.AUTH)

        return expectResponse(reqSpec.then().assertThat().statusCode(is(200)), AuthenticationResponse.class)
    }

    protected final Response sendSignUpRequest(
            final String documentId,
            final ResponseFieldsSnippet respDoc
    ) {
        return sendSignUpRequest(documentId, null, respDoc)
    }

    protected final Response sendSignUpRequest(
            final String documentId,
            final Object request,
            final ResponseFieldsSnippet respDoc
    ) {
        final RequestSpecification reqSpec

        if (request == null) {
            reqSpec = jsonRequestSpec(documentId, respDoc)
                    .when()
        } else {
            reqSpec = jsonRequestSpec(documentId, authRequestFieldsDoc(), respDoc)
                    .when()
                    .body(request)
        }

        return reqSpec.post(ApiPaths.AUTH)
    }

    static RequestFieldsSnippet authRequestFieldsDoc() {
        return requestFields(
                fieldWithPath("loginName")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationRequest.DESC_LOGIN_NAME),
                fieldWithPath("password")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationRequest.DESC_PASSWORD),
                fieldWithPath("platformType")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationRequest.DESC_PLATFORM_TYPE),
                fieldWithPath("platformVersion")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationRequest.DESC_PLATFORM_VERSION),
                fieldWithPath("appVersion")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationRequest.DESC_APP_VERSION)
        )
    }

    /**
     * Since all API error are in same format({@link com.github.fj.board.endpoint.ErrorResponseDto}), this method could be handy for documenting error cases.
     */
    static ResponseFieldsSnippet authResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.loginName")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationResponse.DESC_LOGIN_NAME),
                fieldWithPath("body.accessToken")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationResponse.DESC_ACCESS_TOKEN),
                fieldWithPath("body.accessTokenExpiresAfter")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationResponse.DESC_ACCESS_TOKEN_EXPIRES_AFTER),
                fieldWithPath("body.refreshToken")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationResponse.DESC_REFRESH_TOKEN),
                fieldWithPath("body.refreshTokenExpiresAfter")
                        .type(JsonFieldType.STRING)
                        .description(AuthenticationResponse.DESC_REFRESH_TOKEN_EXPIRES_AFTER)
        ]

        return responseFields(baseFieldDescriptors() + fields)
    }
}

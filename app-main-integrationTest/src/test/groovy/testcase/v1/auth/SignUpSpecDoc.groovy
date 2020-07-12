/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.IntegrationTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpSpecDoc {
    static Response sendSignUpRequest(
            final IntegrationTestBase testContext,
            final String documentId,
            final ResponseFieldsSnippet respDoc
    ) {
        return sendSignUpRequest(testContext, documentId, null, respDoc)
    }

    static Response sendSignUpRequest(
            final IntegrationTestBase testContext,
            final String documentId,
            final Object request,
            final ResponseFieldsSnippet respDoc
    ) {
        final RequestSpecification reqSpec

        if (request == null) {
            reqSpec = testContext.jsonRequestSpec(documentId, respDoc)
                    .when()
        } else {
            reqSpec = testContext.jsonRequestSpec(documentId, requestFieldsDoc(), respDoc)
                    .when()
                    .body(request)
        }

        return reqSpec.post(ApiPaths.AUTH)
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("loginName")
                        .type(JsonFieldType.STRING)
                        .description(SignUpRequest.DESC_LOGIN_NAME),
                fieldWithPath("password")
                        .type(JsonFieldType.STRING)
                        .description(SignUpRequest.DESC_PASSWORD),
                fieldWithPath("platformType")
                        .type(JsonFieldType.STRING)
                        .description(SignUpRequest.DESC_PLATFORM_TYPE),
                fieldWithPath("platformVersion")
                        .type(JsonFieldType.STRING)
                        .description(SignUpRequest.DESC_PLATFORM_VERSION),
                fieldWithPath("appVersion")
                        .type(JsonFieldType.STRING)
                        .description(SignUpRequest.DESC_APP_VERSION)
        )
    }
}

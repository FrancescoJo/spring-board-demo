/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationResponse
import com.github.fj.board.endpoint.v1.auth.dto.RefreshTokenRequest
import com.github.fj.board.exception.client.RefreshTokenMismatchException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.lib.time.DateTimeUtilsKt
import com.github.fj.lib.util.ProtectedProperty
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.AuthTestBase

import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class RefreshAccessTokenSpec extends AuthTestBase {
    @Autowired
    private AuthenticationRepository authRepo

    def "fail if not authenticated"() {

    }

    def "fail if refreshToken mismatches"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(new ProtectedProperty<>(""))

        when:
        final reqSpec = sendRequest(
                "refreshAccessToken-error-refreshTokenMismatch",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(403))).body

        expect:
        errorBody.cause == RefreshTokenMismatchException.class.simpleName
    }

    def "fail if given refreshToken is too old"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(createdAuth.refreshToken)

        and: "set refreshToken expireDate to way-too-far past"
        final savedAuthEntity = authRepo.findByLoginName(createdAuth.loginName)
        savedAuthEntity.refreshTokenIssuedAt = DateTimeUtilsKt.LOCAL_DATE_TIME_MIN
        savedAuthEntity.refreshTokenExpireAt = DateTimeUtilsKt.LOCAL_DATE_TIME_MIN
        authRepo.save(savedAuthEntity)

        when:
        final reqSpec = sendRequest(
                "refreshAccessToken-error-refreshTokenTooOld",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(403))).body

        expect:
        errorBody.cause == RefreshTokenMismatchException.class.simpleName
    }

    def "accessToken is renewed if refreshToken is matched"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(createdAuth.refreshToken)

        when:
        final reqSpec = sendRequest(
                "refreshAccessToken",
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
            final RefreshTokenRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .patch(ApiPaths.TOKEN)
    }

    static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("oldRefreshToken")
                        .type(JsonFieldType.STRING)
                        .description(RefreshTokenRequest.DESC_REFRESH_TOKEN)
        )
    }
}

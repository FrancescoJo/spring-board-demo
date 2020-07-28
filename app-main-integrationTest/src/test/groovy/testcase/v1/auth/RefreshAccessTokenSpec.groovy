/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.request.RefreshTokenRequest
import com.github.fj.board.endpoint.v1.auth.response.AuthenticationResponse
import com.github.fj.board.exception.client.auth.RefreshTokenMismatchException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.lib.time.DateTimeUtilsKt
import com.github.fj.lib.util.ProtectedProperty
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.v1.AuthTestBase

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
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(createdAuth.refreshToken)

        when:
        final response = jsonRequestSpec("refreshAccessToken-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPaths.TOKEN)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if refreshToken mismatches"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(new ProtectedProperty<>(""))

        when:
        final response = sendRequest(
                "refreshAccessToken-error-refreshTokenMismatch",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, RefreshTokenMismatchException.STATUS)

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
        final response = sendRequest(
                "refreshAccessToken-error-refreshTokenTooOld",
                createdAuth.accessToken.value,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, RefreshTokenMismatchException.STATUS)

        expect:
        errorBody.cause == RefreshTokenMismatchException.class.simpleName
    }

    def "accessToken is renewed if refreshToken is matched"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new RefreshTokenRequest(createdAuth.refreshToken)

        when:
        final rawResponse = sendRequest(
                "refreshAccessToken",
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

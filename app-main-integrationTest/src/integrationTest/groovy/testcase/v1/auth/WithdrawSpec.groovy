/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.exception.client.auth.CannotWithdrawException
import com.github.fj.board.exception.generic.UnauthenticatedException
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.v1.AuthTestBase

import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Aug - 2020
 */
class WithdrawSpec extends AuthTestBase {
    def "an unauthenticated user cannot delete account"() {
        when:
        final response = jsonRequestSpec("withdraw-error-unauthenticated", errorResponseFieldsDoc())
                .when()
                .delete(ApiPaths.ACCOUNT)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "authenticated user can delete account"() {
        given:
        final authentication = createRandomAuth()

        when:
        final rawResponse = sendRequest(
                "withdraw",
                authentication.accessToken.value,
                genericBooleanResponseDoc()
        )

        then:
        final response = expectGenericResponse(rawResponse, HttpStatus.OK, Boolean.class)

        expect: "response is true"
        response
    }

    // Any authenticated operations will fail after withdraw
    def "cannot withdraw twice"() {
        given:
        final authentication = createRandomAuth()

        when:
        authenticatedRequest(authentication.accessToken.value).delete(ApiPaths.ACCOUNT)
                .then()
                .assertThat().statusCode(is(HttpStatus.OK.value()))

        then:
        final rawResponse = authenticatedRequest(authentication.accessToken.value).delete(ApiPaths.ACCOUNT)
        final errorBody = expectError(rawResponse, CannotWithdrawException.STATUS)

        expect:
        errorBody.cause == CannotWithdrawException.class.simpleName
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, respDoc)
                .delete(ApiPaths.ACCOUNT)
    }
}

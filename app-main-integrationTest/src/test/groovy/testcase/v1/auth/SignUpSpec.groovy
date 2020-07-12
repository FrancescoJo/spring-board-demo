/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth


import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.persistence.model.auth.PlatformType
import io.restassured.response.Response
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.endpoint.v1.auth.dto.SignUpRequestBuilder
import testcase.IntegrationTestBase

import static org.hamcrest.CoreMatchers.is
import static testcase.v1.auth.SignUpSpecDoc.sendSignUpRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpSpec extends IntegrationTestBase {
    def "empty request payload does nothing"() {
        when:
        final reqSpec = sendRequest("signUp-error-emptyRequest", getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == HttpMessageNotReadableException.class.simpleName
    }

    @Unroll
    def "loginName('#loginName') must be 4 to 16 alphanumeric characters long"() {
        given:
        final request = new SignUpRequestBuilder(SignUpRequestBuilder.createRandom())
                .loginName(loginName)
                .build()

        when:
        final reqSpec = sendRequest("signUp-error-wrongLoginName", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        loginName              | _
        "ad"                   | _
        "asdfasdfasdfasdfadsf" | _
        "n`o`n@lph@^um"        | _
    }

    @Unroll
    def "password('#password') must be 6 to 32 characters long"() {
        given:
        final request = new SignUpRequestBuilder(SignUpRequestBuilder.createRandom())
                .password(password)
                .build()

        when:
        final reqSpec = sendRequest("signUp-error-wrongPassword", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        password                            | _
        ""                                  | _
        "adaaa"                             | _
        "123456789012345678901234567890123" | _
    }

    def "unknown client platformType is not allowed"() {
        given:
        final request = new SignUpRequestBuilder(SignUpRequestBuilder.createRandom())
                .platformType(PlatformType.UNDEFINED)
                .build()

        when:
        final reqSpec = sendRequest("signUp-error-unknownClientPlatform", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "client app version must be formatted as SemanticVersion"() {
        given:
        final request = new SignUpRequestBuilder(SignUpRequestBuilder.createRandom())
                .appVersion("1234567890")
                .buildAsJsonBy(getJsonMapper())

        when:
        final reqSpec = sendRequest("signUp-error-wrongClientAppVersion", request, getErrorResponseFields())

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    // TODO "already occupied loginName cannot be used for signUp"

    // TODO "plain good sign-up request would be successful"

    private Response sendRequest(final String documentId, final ResponseFieldsSnippet respDoc) {
        return sendSignUpRequest(this, documentId, respDoc)
    }

    private Response sendRequest(
            final String documentId,
            final Object request,
            final ResponseFieldsSnippet respDoc
    ) {
        return sendSignUpRequest(this, documentId, request, respDoc)
    }
}

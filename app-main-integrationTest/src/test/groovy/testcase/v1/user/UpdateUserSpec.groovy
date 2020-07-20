/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.user

import com.github.fj.board.endpoint.v1.user.dto.UpdateUserRequest
import com.github.fj.board.endpoint.v1.user.dto.UserInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.generic.UnauthorisedException
import io.restassured.response.Response
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.endpoint.ApiPathsHelper
import test.endpoint.v1.user.dto.UpdateUserRequestBuilder
import testcase.UserTestBase

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString
import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class UpdateUserSpec extends UserTestBase {
    def "fail if not authenticated"() {
        given:
        final request = UpdateUserRequestBuilder.createRandom()

        when:
        final reqSpec = jsonRequestSpec("updateUser-error-unauthorised", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPathsHelper.USER_NICKNAME(getRandomAlphaNumericString(8)))

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(401))).body

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if authenticated user is not target user to be updated"() {
        given:
        final targetUser = createRandomUser()
        final self = createRandomUser()
        final request = UpdateUserRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateUser-error-notTargetUser",
                self.accessToken,
                targetUser.nickname,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(403))).body

        expect:
        errorBody.cause == UnauthorisedException.class.simpleName
    }

    @Unroll
    def "fail if nickname('#nickname') is too long or too short"() {
        given:
        final createdUser = createRandomUser()
        final request = new UpdateUserRequestBuilder(UpdateUserRequestBuilder.createRandom())
                .nickname(nickname)
                .build()

        when:
        final reqSpec = sendRequest(
                "updateUser-error-wrongNickname-#$docId",
                createdUser.accessToken,
                createdUser.nickname,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        nickname    | docId
        ""          | 1
        "1"         | 2
        "123456789" | 3
    }

    @Unroll
    def "fail if email('#email') is in wrong format or too long"() {
        given:
        final self = createRandomUser()
        final request = new UpdateUserRequestBuilder(UpdateUserRequestBuilder.createRandom())
                .email(email)
                .build()

        when:
        final reqSpec = sendRequest(
                "updateUser-error-wrongEmail-#$docId",
                self.accessToken,
                self.nickname,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName

        where:
        email                                          | docId
        ""                                             | 1
        "wrong_email"                                  | 2
        "name@${getRandomAlphaNumericString(120)}.com" | 3
    }

    def "user is updated if request is valid"() {
        given:
        final self = createRandomUser()
        final request = UpdateUserRequestBuilder.createRandom()

        when:
        final reqSpec = sendRequest(
                "updateUser",
                self.accessToken,
                self.nickname,
                request,
                userInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), UserInfoResponse.class)

        expect:
        response.nickname == request.nickname
        response.email == (request.email ?: "")
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String targetNickname,
            final UpdateUserRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .patch(ApiPathsHelper.USER_NICKNAME(targetNickname))
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("nickname")
                        .type(JsonFieldType.STRING)
                        .description(UpdateUserRequest.DESC_NICKNAME),
                fieldWithPath("email")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(UpdateUserRequest.DESC_EMAIL)
        )
    }
}

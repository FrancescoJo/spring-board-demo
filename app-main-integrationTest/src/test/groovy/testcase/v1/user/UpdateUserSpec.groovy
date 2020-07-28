/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.user

import com.github.fj.board.endpoint.v1.user.request.UpdateUserRequest
import com.github.fj.board.endpoint.v1.user.response.UserInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.generic.UnauthorisedException
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.user.dto.UpdateUserRequestBuilder
import testcase.v1.UserTestBase

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString
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
        final response = jsonRequestSpec("updateUser-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .patch(ApiPathsHelper.USER_NICKNAME(getRandomAlphaNumericString(8)))

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if authenticated user is not target user to be updated"() {
        given:
        final targetUser = createRandomUser()
        final self = createRandomUser()
        final request = UpdateUserRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updateUser-error-notTargetUser",
                self.accessToken,
                targetUser.nickname,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, UnauthorisedException.STATUS)

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
        final response = sendRequest(
                "updateUser-error-wrongNickname-#$docId",
                createdUser.accessToken,
                createdUser.nickname,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

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
        final response = sendRequest(
                "updateUser-error-wrongEmail-#$docId",
                self.accessToken,
                self.nickname,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

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
        final rawResponse = sendRequest(
                "updateUser",
                self.accessToken,
                self.nickname,
                request,
                userInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, UserInfoResponse.class)

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

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.user

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.request.CreateUserRequest
import com.github.fj.board.endpoint.v1.user.response.UserInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.user.NicknameAlreadyExistException
import com.github.fj.board.exception.generic.UnauthenticatedException
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.v1.user.dto.CreateUserRequestBuilder
import testcase.v1.UserTestBase

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
class CreateUserSpec extends UserTestBase {
    def "fail if not authenticated"() {
        given:
        final request = CreateUserRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("createUser-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .post(ApiPaths.USER)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    @Unroll
    def "fail if nickname('#nickname') is too long or too short"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new CreateUserRequestBuilder(CreateUserRequestBuilder.createRandom())
                .nickname(nickname)
                .build()

        when:
        final response = sendRequest(
                "createUser-error-wrongNickname-#$docId",
                createdAuth.accessToken.value,
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
        final createdAuth = createRandomAuth()
        final request = new CreateUserRequestBuilder(CreateUserRequestBuilder.createRandom())
                .email(email)
                .build()

        when:
        final response = sendRequest(
                "createUser-error-wrongEmail-#$docId",
                createdAuth.accessToken.value,
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

    @Unroll
    def "user is created if request is valid with nickname('#nickname')"() {
        given:
        final createdAuth = createRandomAuth()
        final request = new CreateUserRequestBuilder(CreateUserRequestBuilder.createRandom())
                .nickname(nickname)
                .build()

        when:
        final rawResponse = sendRequest(
                "createUser-#$docId",
                createdAuth.accessToken.value,
                request,
                userInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, UserInfoResponse.class)

        expect:
        response.nickname == request.nickname
        response.email == (request.email ?: "")

        where:
        nickname               | docId
        "Judy💚"               | 1
        "ตีสอง"                | 2
        "小泉進次郎"                | 3
        "Eugein🏴󠁧󠁢󠁷󠁬󠁳󠁿" | 4
        "한글"                   | 5
    }

    def "fail if nickname is duplicated"() {
        given:
        final createdUser = createRandomUser()
        final String email
        if (createdUser.email.isEmpty()) {
            email = null
        } else {
            email = createdUser.email
        }

        and:
        final request = new CreateUserRequestBuilder()
                .nickname(createdUser.nickname)
                .email(email)
                .build()

        when:
        final rawResponse = sendRequest(
                "createUser-error-duplicateNickname",
                createdUser.accessToken,
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(rawResponse, NicknameAlreadyExistException.STATUS)

        expect:
        errorBody.cause == NicknameAlreadyExistException.class.simpleName
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final CreateUserRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .post(ApiPaths.USER)
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("nickname")
                        .type(JsonFieldType.STRING)
                        .description(CreateUserRequest.DESC_NICKNAME),
                fieldWithPath("email")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(CreateUserRequest.DESC_EMAIL),
                fieldWithPath("invitedBy")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(CreateUserRequest.DESC_INVITED_BY)
        )
    }
}

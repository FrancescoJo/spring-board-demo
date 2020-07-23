/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.dto.UserInfoResponse
import com.github.fj.board.persistence.model.user.Status
import com.github.fj.board.persistence.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.v1.user.dto.CreateUserRequestBuilder

import java.time.LocalDateTime

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
class UserTestBase extends AuthTestBase {
    @Autowired
    private UserRepository userRepo

    def cleanup() {
        userRepo.deleteAll()
    }

    protected final CreatedUser createRandomUser() {
        final createdAuth = createRandomAuth()
        final request = CreateUserRequestBuilder.createRandom()
        final rawResponse = authenticatedRequest(createdAuth.accessToken.value)
                .body(request)
                .post(ApiPaths.USER)

        final response = expectResponse(rawResponse, HttpStatus.OK, UserInfoResponse.class)

        return new CreatedUser(
                /*loginName */                 createdAuth.loginName,
                /*accessToken */               createdAuth.accessToken.value,
                /* accessTokenExpiresAfter */  createdAuth.accessTokenExpiresAfter,
                /* refreshToken */             createdAuth.refreshToken.value,
                /* refreshTokenExpiresAfter */ createdAuth.refreshTokenExpiresAfter,
                /* nickname */                 response.nickname,
                /* status */                   response.status,
                /* email */                    response.email,
                /* createdDate */              response.createdDate,
                /* lastActiveDate */           response.lastActiveDate
        )
    }

    protected static ResponseFieldsSnippet userInfoResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.nickname")
                        .type(JsonFieldType.STRING)
                        .description(UserInfoResponse.DESC_NICKNAME),
                fieldWithPath("body.status")
                        .type(JsonFieldType.STRING)
                        .description(UserInfoResponse.DESC_STATUS),
                fieldWithPath("body.email")
                        .type(JsonFieldType.STRING)
                        .description(UserInfoResponse.DESC_EMAIL),
                fieldWithPath("body.createdDate")
                        .type(JsonFieldType.STRING)
                        .description(UserInfoResponse.DESC_CREATED_DATE),
                fieldWithPath("body.lastActiveDate")
                        .type(JsonFieldType.STRING)
                        .description(UserInfoResponse.DESC_LAST_ACTIVE_DATE)
        ]

        return responseFields(basicFieldsDoc() + fields)
    }

    static class CreatedUser {
        final String loginName
        final String accessToken
        final LocalDateTime accessTokenExpiresAfter
        final String refreshToken
        final LocalDateTime refreshTokenExpiresAfter
        final String nickname
        final Status status
        final String email
        final LocalDateTime createdDate
        final LocalDateTime lastActiveDate

        CreatedUser(
                final String loginName,
                final String accessToken,
                final LocalDateTime accessTokenExpiresAfter,
                final String refreshToken,
                final LocalDateTime refreshTokenExpiresAfter,
                final String nickname,
                final Status status,
                final String email,
                final LocalDateTime createdDate,
                final LocalDateTime lastActiveDate
        ) {
            this.loginName = loginName
            this.accessToken = accessToken
            this.accessTokenExpiresAfter = accessTokenExpiresAfter
            this.refreshToken = refreshToken
            this.refreshTokenExpiresAfter = refreshTokenExpiresAfter
            this.nickname = nickname
            this.status = status
            this.email = email
            this.createdDate = createdDate
            this.lastActiveDate = lastActiveDate
        }

        @Override
        String toString() {
            return "CreatedUser{" +
                    "loginName='$loginName'" +
                    ", accessToken='$accessToken'" +
                    ", accessTokenExpiresAfter=$accessTokenExpiresAfter" +
                    ", refreshToken='$refreshToken" +
                    ", refreshTokenExpiresAfter=$refreshTokenExpiresAfter" +
                    ", nickname='$nickname'" +
                    ", status='$status'" +
                    ", email='$email'" +
                    ", createdDate='$createdDate'" +
                    ", lastActiveDate='$lastActiveDate'" +
                    '}'
        }
    }
}

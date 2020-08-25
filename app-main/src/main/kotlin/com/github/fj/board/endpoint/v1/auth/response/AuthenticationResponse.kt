/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.lib.util.ProtectedProperty
import java.time.LocalDateTime

/**
 * A sign-up response to confirm user's last sign-up request was successful.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@JsonSerialize
data class AuthenticationResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_LOGIN_NAME)
    val loginName: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_ACCESS_TOKEN)
    val accessToken: ProtectedProperty<String>,

    @JsonProperty
    @JsonPropertyDescription(DESC_ACCESS_TOKEN_EXPIRES_AFTER)
    val accessTokenExpiresAfter: LocalDateTime,

    @JsonProperty
    @JsonPropertyDescription(DESC_REFRESH_TOKEN)
    val refreshToken: ProtectedProperty<String>,

    @JsonProperty
    @JsonPropertyDescription(DESC_REFRESH_TOKEN_EXPIRES_AFTER)
    val refreshTokenExpiresAfter: LocalDateTime
) {
    companion object {
        const val DESC_LOGIN_NAME = "A 'user id' that specified by user."
        const val DESC_ACCESS_TOKEN = "An access token object for authentication. " +
                "Users should keep this information carefully and do not expose it to public."
        const val DESC_ACCESS_TOKEN_EXPIRES_AFTER = "A UNIX timestamp for access token expiration. "
        const val DESC_REFRESH_TOKEN = "A value to refresh old access token object for authentication. " +
                "Users should keep this information carefully and do not expose it to public. " +
                "Both `accessToken` and `refreshToken` are used for extending login status."
        const val DESC_REFRESH_TOKEN_EXPIRES_AFTER = "A UNIX timestamp represents refresh token expiration."

        fun from(vo: AuthenticationResult) = with(vo) {
            AuthenticationResponse(
                loginName = loginName,
                accessToken = ProtectedProperty(accessToken),
                accessTokenExpiresAfter = accessTokenExpiresAfter,
                refreshToken = ProtectedProperty(refreshToken),
                refreshTokenExpiresAfter = refreshTokenExpiresAfter
            )
        }
    }
}

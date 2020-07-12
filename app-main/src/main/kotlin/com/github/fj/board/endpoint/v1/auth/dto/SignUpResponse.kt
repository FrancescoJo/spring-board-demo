/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.vo.auth.SignUpResult
import com.github.fj.lib.util.ProtectedProperty
import java.time.LocalDateTime

/**
 * A sign-up response to confirm user's last sign-up request was successful.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@JsonSerialize
data class SignUpResponse(
    /**
     * 'user id' that specified by user.
     */
    @JsonProperty
    val loginName: String,

    /**
     * An access token object for authentication. Users should keep this information carefully and do not expose it
     * to public.
     */
    @JsonProperty
    val accessToken: ProtectedProperty<String>,

    val accessTokenExpiresAfter: LocalDateTime,

    /**
     * An refresh access token object for authentication. Users should keep this information carefully and do not
     * expose it to public. Refreshing [accessToken] requires this value as well.
     */
    @JsonProperty
    val refreshToken: ProtectedProperty<String>,

    val refreshTokenExpiresAfter: LocalDateTime
) {
    companion object {
        fun from(vo: SignUpResult) = with(vo) {
            SignUpResponse(
                loginName = loginName,
                accessToken = ProtectedProperty(accessToken),
                accessTokenExpiresAfter = accessTokenExpiresAfter,
                refreshToken = ProtectedProperty(refreshToken),
                refreshTokenExpiresAfter = refreshTokenExpiresAfter
            )
        }
    }
}

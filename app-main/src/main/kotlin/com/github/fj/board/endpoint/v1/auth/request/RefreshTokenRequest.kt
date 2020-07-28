/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.github.fj.lib.util.ProtectedProperty

/**
 * Refresh token request to update authentication.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
data class RefreshTokenRequest(
    @JsonProperty
    @JsonPropertyDescription(DESC_REFRESH_TOKEN)
    val oldRefreshToken: ProtectedProperty<String>
) {
    companion object {
        const val DESC_REFRESH_TOKEN = "Refresh token which is previously issued to user."
    }
}

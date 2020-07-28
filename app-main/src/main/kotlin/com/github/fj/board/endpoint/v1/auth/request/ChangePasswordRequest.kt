/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.github.fj.lib.util.ProtectedProperty

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
data class ChangePasswordRequest(
    @JsonProperty
    @JsonPropertyDescription(DESC_OLD_PASSWORD)
    val oldPassword: ProtectedProperty<String>,

    @JsonProperty
    @JsonPropertyDescription(DESC_NEW_PASSWORD)
    val newPassword: ProtectedProperty<String>
) {
    companion object {
        const val DESC_OLD_PASSWORD = "SHA-1 hex encoded previous password text."
        const val DESC_NEW_PASSWORD = "SHA-1 hex encoded new password text. Must be different to old one."
    }
}

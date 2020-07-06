/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.fj.lib.util.ProtectedProperty

/**
 * A sign-up response to confirm user's last sign-up request was successful.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
data class SignUpResponse(
    /**
     * 'user id' that specified by user.
     */
    @JsonProperty
    val loginName: String,

    /**
     * An access token object for authentication. Users should keep this information carefully and do not expose it
     * to other people.
     */
    @JsonProperty
    val accessToken: ProtectedProperty<String>
)

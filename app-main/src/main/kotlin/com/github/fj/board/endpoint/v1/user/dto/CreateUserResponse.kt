/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * Echoing back response of [CreateUserRequest] except invitation info.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@JsonSerialize
data class CreateUserResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    val email: String?
) {
    companion object {
        const val DESC_NICKNAME = "Nickname that user decided and accepted."
        const val DESC_EMAIL = "Email address that user provided."
    }
}

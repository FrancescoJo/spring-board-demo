/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.user.Gender

/**
 * Echoing back response of [CreateUserRequest] except invitation info.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@JsonSerialize
data class CreateUserResponse(
    val nickname: String,

    @JsonInclude(JsonInclude.Include.ALWAYS)
    val email: String?,

    @JsonInclude(JsonInclude.Include.ALWAYS)
    val gender: Gender?
)

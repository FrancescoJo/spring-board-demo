/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.persistence.model.user.Gender
import com.github.fj.board.validation.ProtectedPropertySize
import javax.validation.constraints.Email
import javax.validation.constraints.Size

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@JsonDeserialize
data class CreateUserRequest(
    @ProtectedPropertySize(
        min = NICKNAME_SIZE_MIN,
        max = NICKNAME_SIZE_MAX,
        message = "nickname must between $NICKNAME_SIZE_MIN to $NICKNAME_SIZE_MAX characters."
    )
    val nickname: String,

    /**
     * Email address. No longer than 128 characters, and optional.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(
        max = EMAIL_SIZE_MAX,
        message = "email cannot be longer than $EMAIL_SIZE_MAX characters."
    )
    @Email
    val email: String?,

    /**
     * User gender. Optional value.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val gender: Gender?,

    /**
     * nickname of other user whom made this invitation.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val invitedBy: String?
) {
    companion object {
        const val NICKNAME_SIZE_MIN = 2L
        const val NICKNAME_SIZE_MAX = 8L
        const val EMAIL_SIZE_MAX = 128
    }
}

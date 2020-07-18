/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.endpoint.v1.auth.dto.RefreshTokenRequest
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.model.user.Gender
import com.github.fj.board.validation.ProtectedPropertySize
import com.github.fj.board.validation.UnicodeCharsLength
import javax.validation.constraints.Email
import javax.validation.constraints.Size

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@JsonDeserialize
data class CreateUserRequest(
    @UnicodeCharsLength(
        min = NICKNAME_SIZE_MIN,
        max = NICKNAME_SIZE_MAX,
        message = "nickname must between $NICKNAME_SIZE_MIN to $NICKNAME_SIZE_MAX characters."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(
        max = EMAIL_SIZE_MAX,
        message = "email cannot be longer than $EMAIL_SIZE_MAX characters."
    )
    @Email
    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    val email: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty
    @JsonPropertyDescription(DESC_INVITED_BY)
    val invitedBy: String?
) {
    companion object {
        const val NICKNAME_SIZE_MIN = 2
        const val NICKNAME_SIZE_MAX = 8
        const val EMAIL_SIZE_MAX = 128

        const val DESC_NICKNAME = "A distinct name to identify users. Must between " +
                "$NICKNAME_SIZE_MIN to $NICKNAME_SIZE_MAX characters long."
        const val DESC_EMAIL = "(Optional) Email address. No longer than $EMAIL_SIZE_MAX characters."
        const val DESC_INVITED_BY = "(Optional) `loginName` of other user whom made this invitation."
    }
}

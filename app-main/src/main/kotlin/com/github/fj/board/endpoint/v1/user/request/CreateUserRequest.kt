/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.validation.UnicodeCharsLength
import javax.validation.constraints.Email
import javax.validation.constraints.Size

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@JsonDeserialize
data class CreateUserRequest(
    @get:UnicodeCharsLength(
        min = NICKNAME_SIZE_MIN,
        max = NICKNAME_SIZE_MAX,
        message = "`nickname` must between $NICKNAME_SIZE_MIN and $NICKNAME_SIZE_MAX characters."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:Size(
        min = EMAIL_SIZE_MIN,
        max = EMAIL_SIZE_MAX,
        message = "`email` must between $EMAIL_SIZE_MIN and $EMAIL_SIZE_MAX characters."
    )
    @get:Email(message = "Not a valid email format.")
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

        /**
         * The smallest possible length by [RFC 3696](https://tools.ietf.org/html/rfc3696#section-3).
         */
        const val EMAIL_SIZE_MIN = 3
        const val EMAIL_SIZE_MAX = 128

        const val DESC_NICKNAME = "A distinct name to identify users. Must between " +
                "$NICKNAME_SIZE_MIN and $NICKNAME_SIZE_MAX characters long."
        const val DESC_EMAIL = "(Optional) Email address. No longer than $EMAIL_SIZE_MAX characters."
        const val DESC_INVITED_BY = "(Optional) `loginName` of other user who made this invitation."
    }
}

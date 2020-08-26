/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.user.UserStatus
import com.github.fj.board.vo.user.UserInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
@JsonSerialize
data class UserInfoResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_STATUS)
    val status: UserStatus,

    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    val email: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_CREATED_DATE)
    val createdDate: LocalDateTime,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_ACTIVE_DATE)
    val lastActiveDate: LocalDateTime
) {
    companion object {
        const val DESC_NICKNAME = "Nickname that user decided and accepted."
        const val DESC_STATUS =
            "User's current status. Read link:#common-types-userStatus[`UserStatus`] for more details."
        const val DESC_EMAIL = "Email address that user provided. Empty if no email is registered."
        const val DESC_CREATED_DATE = "The very first date when this user started activities."
        const val DESC_LAST_ACTIVE_DATE = "The very last date when this user ended activities."

        fun from(userInfo: UserInfo) = with(userInfo) {
            UserInfoResponse(
                nickname = nickname,
                status = status,
                email = email,
                createdDate = createdDate,
                lastActiveDate = lastActiveDate
            )
        }
    }
}

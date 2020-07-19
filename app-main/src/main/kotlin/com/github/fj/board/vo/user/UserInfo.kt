/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.user

import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.user.Status
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
data class UserInfo(
    val nickname: String,
    val status: Status,
    val email: String,
    val createdDate: LocalDateTime,
    val lastActiveDate: LocalDateTime
) {
    companion object {
        fun from(src: User) = with(src) {
            UserInfo(
                nickname = nickname,
                status = status,
                email = email,
                createdDate = createdDate,
                lastActiveDate = lastActiveDate
            )
        }
    }
}

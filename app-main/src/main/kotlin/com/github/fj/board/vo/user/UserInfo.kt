/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.user

import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.user.UserStatus
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
data class UserInfo(
    val loginName: String,
    val nickname: String,
    val status: UserStatus,
    val email: String,
    val createdDate: LocalDateTime,
    val lastActiveDate: LocalDateTime
) {
    companion object {
        fun from(src: User) = UserInfo(
            loginName = src.authentication.loginName,
            nickname = src.nickname,
            status = src.status,
            email = src.email,
            createdDate = src.createdDate,
            lastActiveDate = src.lastActiveDate
        )
    }
}

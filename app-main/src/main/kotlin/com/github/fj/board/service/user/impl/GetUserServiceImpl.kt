/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user.impl

import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.GetUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.user.UserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
@Service
internal class GetUserServiceImpl(
    private val userRepo: UserRepository
) : GetUserService {
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun get(nickname: String, clientInfo: ClientAuthInfo): UserInfo {
        val user = userRepo.findByNickname(nickname) ?: throw UserNotFoundException()

        return UserInfo.from(user)
    }
}

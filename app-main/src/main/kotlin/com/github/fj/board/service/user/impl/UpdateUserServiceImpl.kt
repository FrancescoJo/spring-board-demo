/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user.impl

import com.github.fj.board.endpoint.v1.user.request.UpdateUserRequest
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.UpdateUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.user.UserInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@Service
internal class UpdateUserServiceImpl(
    override val userRepo: UserRepository
) : UpdateUserService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun update(nickname: String, req: UpdateUserRequest, clientInfo: ClientAuthInfo): UserInfo {
        val targetUser = userRepo.findByNickname(nickname) ?: throw UserNotFoundException()
        val self = clientInfo.findCurrentAccessibleUser()

        if (targetUser != self) {
            throw UnauthorisedException()
        }

        if (!req.isUpdating(self)) {
            return UserInfo.from(self)
        }

        val updatedUser = self.apply {
            this.nickname = req.nickname
            this.email = req.email ?: ""

            applyLastActivityWith(clientInfo, utcNow())
        }

        return UserInfo.from(updatedUser).also {
            userRepo.save(updatedUser)
        }
    }

    private fun UpdateUserRequest.isUpdating(self: User): Boolean =
        this.nickname != self.nickname || this.email ?: "" != self.email
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user.impl

import com.github.fj.board.endpoint.v1.user.request.CreateUserRequest
import com.github.fj.board.exception.client.auth.LoginNameNotFoundException
import com.github.fj.board.exception.client.user.NicknameAlreadyExistException
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.user.UserStatus
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.CreateUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.user.UserInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
@Service
internal class CreateUserServiceImpl(
    override val userRepo: UserRepository,
    private val authRepo: AuthenticationRepository
) : CreateUserService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun create(req: CreateUserRequest, clientInfo: ClientAuthInfo): UserInfo {
        val auth = authRepo.findByLoginName(clientInfo.loginName) ?: run {
            throw LoginNameNotFoundException()
        }

        if (userRepo.findByNickname(req.nickname) != null) {
            throw NicknameAlreadyExistException()
        }

        val now = utcNow()

        val createdUser = User().apply {
            this.authentication = auth
            this.nickname = req.nickname
            this.status = UserStatus.UNVERIFIED
            this.email = req.email ?: ""
            this.createdDate = now
            this.createdIp = clientInfo.remoteAddr

            applyLastActivityWith(clientInfo, now)

            this.invitedBy = req.invitedBy.takeIf { !it.isNullOrEmpty() }?.let {
                return@let userRepo.findByNickname(it)
            }
        }

        return UserInfo.from(createdUser).also {
            userRepo.save(createdUser)
        }
    }
}

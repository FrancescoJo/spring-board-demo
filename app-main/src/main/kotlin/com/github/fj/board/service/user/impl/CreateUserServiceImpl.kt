/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user.impl

import com.github.fj.board.endpoint.v1.user.dto.CreateUserRequest
import com.github.fj.board.exception.client.auth.LoginNameNotFoundException
import com.github.fj.board.exception.client.user.NicknameAlreadyExistException
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.user.Status
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.CreateUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
@Service
class CreateUserServiceImpl(
    private val authRepo: AuthenticationRepository,
    private val userRepo: UserRepository
) : CreateUserService {
    @Transactional
    override fun create(req: CreateUserRequest, clientInfo: ClientAuthInfo) {
        val auth = authRepo.findByLoginName(clientInfo.loginName) ?: run {
            throw LoginNameNotFoundException()
        }

        if (userRepo.findByNickname(req.nickname) != null) {
            throw NicknameAlreadyExistException()
        }

        val now = utcNow()

        userRepo.save(User().apply {
            this.authentication = auth
            this.nickname = req.nickname
            this.status = Status.UNVERIFIED
            this.email = req.email ?: ""
            this.createdDate = now
            this.createdIp = clientInfo.remoteAddr
            this.lastActiveDate = now
            this.lastActiveIp = clientInfo.remoteAddr
            this.lastActivePlatformType = clientInfo.platformType
            this.lastActivePlatformVersion = clientInfo.platformVer
            this.lastActiveAppVersion = clientInfo.appVer
            this.invitedBy = req.invitedBy.takeIf { !it.isNullOrEmpty() }?.let {
                return@let userRepo.findByNickname(it)
            }
        })
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth.impl

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.endpoint.v1.auth.request.ChangePasswordRequest
import com.github.fj.board.exception.client.auth.DuplicatedPasswordException
import com.github.fj.board.exception.client.auth.LoginNameNotFoundException
import com.github.fj.board.exception.client.auth.WrongPasswordException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.ChangePasswordService
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientAuthInfo
import io.seruco.encoding.base62.Base62
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
@Service
internal class ChangePasswordServiceImpl(
    override val authTokenMgr: AuthTokenManager,
    override val base62Codec: Base62,
    override val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository
) : ChangePasswordService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun changePassword(
        req: ChangePasswordRequest,
        clientInfo: ClientAuthInfo,
        timestamp: LocalDateTime
    ): AuthenticationResult {
        val auth = authRepo.findByLoginName(clientInfo.loginName) ?: run {
            // Guard case
            throw LoginNameNotFoundException()
        }

        when {
            !auth.isMatch(req.oldPassword) -> throw WrongPasswordException()
            auth.isMatch(req.newPassword)  -> throw DuplicatedPasswordException()
        }

        val token = with(auth) {
            password = hash(req.newPassword)

            return@with updateTokens(timestamp, clientInfo)
        }

        return createAuthResultBy(auth, token).also {
            authRepo.save(auth)
        }
    }
}

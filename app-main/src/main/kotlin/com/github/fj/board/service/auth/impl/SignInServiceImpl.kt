/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth.impl

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.endpoint.v1.auth.request.AuthenticationRequest
import com.github.fj.board.exception.client.auth.LoginNotAllowedException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.SignInService
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientAuthInfo
import io.seruco.encoding.base62.Base62
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@Service
internal class SignInServiceImpl(
    override val authTokenMgr: AuthTokenManager,
    override val base62Codec: Base62,
    override val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository
) : SignInService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun signIn(
        req: AuthenticationRequest,
        clientInfo: ClientAuthInfo,
        timestamp: LocalDateTime
    ): AuthenticationResult {
        val auth = authRepo.findByLoginName(req.loginName) ?: run {
            LOG.info("LoginName is not found: '{}'", req.loginName)
            throw LoginNotAllowedException()
        }

        if (!auth.isMatch(req.password)) {
            LOG.info("Wrong password for user '{}'", req.loginName)
            throw LoginNotAllowedException()
        }

        val token = auth.updateTokens(timestamp, clientInfo)

        return createAuthResultBy(auth, token).also {
            authRepo.save(auth)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignInService::class.java)
    }
}

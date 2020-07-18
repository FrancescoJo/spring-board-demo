/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth.impl

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.exception.client.LoginNotAllowedException
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.SignUpService
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientRequestInfo
import com.github.fj.lib.time.utcNow
import io.seruco.encoding.base62.Base62
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@Service
internal class SignUpServiceImpl(
    override val authTokenMgr: AuthTokenManager,
    override val base62Codec: Base62,
    override val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository
) : SignUpService {
    override fun signUp(req: AuthenticationRequest, clientInfo: ClientRequestInfo): AuthenticationResult {
        if (authRepo.findByLoginName(req.loginName) != null) {
            LOG.info("Duplicated login name: {}", req.loginName)
            throw LoginNotAllowedException()
        }

        val now = utcNow()
        val auth = Authentication().apply {
            loginName = req.loginName
            password = hash(req.password)
        }
        val token = auth.updateTokens(now, clientInfo)

        return createAuthResultBy(auth, token).also {
            authRepo.save(auth)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignUpService::class.java)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.exception.client.LoginNotAllowedException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.util.extractInetAddress
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.lib.security.toSha256Bytes
import io.seruco.encoding.base62.Base62
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@Service
internal class SignInServiceImpl(
    override val authTokenMgr: AuthTokenManager,
    override val base62Encoder: Base62,
    private val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository
) : SignInService {
    @Transactional
    override fun signIn(
        req: AuthenticationRequest,
        httpReq: HttpServletRequest,
        timestamp: LocalDateTime
    ): AuthenticationResult {
        val auth = authRepo.findByLoginName(req.loginName) ?: run {
            LOG.info("LoginName is not found: '{}'", req.loginName)
            throw LoginNotAllowedException()
        }

        val expectedPassword = req.password.value.toSha256Bytes()
        if (!auth.password.contentEquals(expectedPassword)) {
            LOG.info("Wrong password for user '{}'", req.loginName)
            throw LoginNotAllowedException()
        }

        val token = auth.deriveAccessTokenAt(timestamp)

        auth.apply {
            createRefreshToken(timestamp, authProps.refreshTokenAliveDays)

            lastActiveDate = timestamp
            lastActiveIp = httpReq.extractInetAddress()
            lastActivePlatformType = req.platformType
            lastActivePlatformVersion = req.platformVersion
            lastActiveAppVersion = req.appVersion
        }

        return createAuthResultBy(auth, token).also {
            authRepo.save(auth)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignInService::class.java)
    }
}

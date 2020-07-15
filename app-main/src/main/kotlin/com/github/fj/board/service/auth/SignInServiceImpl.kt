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
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.lib.security.toSha256Bytes
import io.seruco.encoding.base62.Base62
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@Service
internal class SignInServiceImpl(
    private val base62Encoder: Base62,
    private val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository,
    private val authTokenMgr: AuthTokenManager
) : SignInService {
    @Transactional
    override fun signIn(req: AuthenticationRequest, httpReq: HttpServletRequest): AuthenticationResult {
        val auth = authRepo.findByLoginName(req.loginName) ?: run {
            LOG.info("LoginName is not found: '{}'", req.loginName)
            throw LoginNotAllowedException()
        }

        val expectedPassword = req.password.value.toSha256Bytes()
        if (!auth.password.contentEquals(expectedPassword)) {
            LOG.info("Wrong password for user '{}'", req.loginName)
            throw LoginNotAllowedException()
        }

        TODO("Not yet implemented")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignInService::class.java)
    }
}

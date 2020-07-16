/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.endpoint.v1.auth.dto.RefreshTokenRequest
import com.github.fj.board.exception.client.LoginNameNotFoundException
import com.github.fj.board.exception.client.RefreshTokenMismatchException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientRequestInfo
import io.seruco.encoding.base62.Base62
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@Service
class RefreshAccessTokenServiceImpl(
    override val authTokenMgr: AuthTokenManager,
    override val base62Codec: Base62,
    override val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository
) : RefreshAccessTokenService {
    override fun refreshAuthToken(
        req: RefreshTokenRequest,
        clientInfo: ClientRequestInfo,
        timestamp: LocalDateTime
    ): AuthenticationResult {
        val auth = authRepo.findByLoginName(clientInfo.loginName) ?: run {
            // Guard case
            throw LoginNameNotFoundException()
        }

        val givenRefreshToken = req.oldRefreshToken.value.toByteArray()

        if (!auth.validateRefreshToken(givenRefreshToken)) {
            throw RefreshTokenMismatchException()
        }

        val token = auth.updateTokens(timestamp, clientInfo)

        return createAuthResultBy(auth, token).also {
            authRepo.save(auth)
        }
    }
}

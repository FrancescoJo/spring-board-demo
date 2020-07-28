/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.endpoint.v1.auth.request.RefreshTokenRequest
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
interface RefreshAccessTokenService : AuthenticationServiceMixin {
    fun refreshAuthToken(
        req: RefreshTokenRequest,
        clientInfo: ClientAuthInfo,
        timestamp: LocalDateTime = utcNow()
    ): AuthenticationResult
}

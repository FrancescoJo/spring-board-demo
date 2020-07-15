/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.lib.time.utcNow
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
interface RefreshAccessTokenService : AuthenticationService {
    fun refreshAuthToken(
        loginName: String,
        req: AuthenticationRequest,
        httpReq: HttpServletRequest,
        timestamp: LocalDateTime = utcNow()
    ): AuthenticationResult
}

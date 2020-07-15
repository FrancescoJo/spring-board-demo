/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.vo.auth.AuthenticationResult
import io.seruco.encoding.base62.Base62
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@Service
class RefreshAccessTokenServiceImpl(
    override val authTokenMgr: AuthTokenManager,
    override val base62Encoder: Base62
) : RefreshAccessTokenService {
    override fun refreshAuthToken(
        loginName: String,
        req: AuthenticationRequest,
        httpReq: HttpServletRequest,
        timestamp: LocalDateTime
    ): AuthenticationResult {
        TODO("Not yet implemented")
    }
}

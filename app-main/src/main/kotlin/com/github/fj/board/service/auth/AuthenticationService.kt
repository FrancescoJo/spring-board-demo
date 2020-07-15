/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.vo.auth.AuthenticationResult
import io.seruco.encoding.base62.Base62
import java.time.LocalDateTime

/**
 * Mix-in for various authentication logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
interface AuthenticationService {
    val authTokenMgr: AuthTokenManager
    val base62Encoder: Base62

    fun Authentication.deriveAccessTokenAt(timestamp: LocalDateTime): FreshHttpAuthorizationToken =
        authTokenMgr.create(loginName, "", timestamp)

    fun createAuthResultBy(auth: Authentication, token: FreshHttpAuthorizationToken): AuthenticationResult =
        AuthenticationResult(
            loginName = auth.loginName,
            accessToken = token.credentials,
            accessTokenExpiresAfter = token.expiration,
            refreshToken = String(base62Encoder.encode(auth.refreshToken)),
            refreshTokenExpiresAfter = auth.refreshTokenExpireAt
        )
}

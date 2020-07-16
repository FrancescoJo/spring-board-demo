/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientRequestInfo
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
    val base62Codec: Base62
    val authProps: AppAuthProperties

    fun Authentication.updateTokens(
        timestamp: LocalDateTime,
        clientInfo: ClientRequestInfo
    ): FreshHttpAuthorizationToken {
        val token = authTokenMgr.create(loginName, "", timestamp)

        createRefreshToken(timestamp, authProps.refreshTokenAliveDays)

        this.lastActiveDate = timestamp
        this.lastActiveIp = clientInfo.remoteAddr
        this.lastActivePlatformType = clientInfo.platformType
        this.lastActivePlatformVersion = clientInfo.platformVer
        this.lastActiveAppVersion = clientInfo.appVer

        return token
    }

    fun createAuthResultBy(auth: Authentication, token: FreshHttpAuthorizationToken): AuthenticationResult =
        AuthenticationResult(
            loginName = auth.loginName,
            accessToken = token.credentials,
            accessTokenExpiresAfter = token.expiration,
            refreshToken = String(base62Codec.encode(auth.refreshToken)),
            refreshTokenExpiresAfter = auth.refreshTokenExpireAt
        )
}

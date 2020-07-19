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
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getSecureRandomBytes
import io.seruco.encoding.base62.Base62
import java.time.LocalDateTime

/**
 * Mix-in for various authentication logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
interface AuthenticationServiceMixin {
    val authTokenMgr: AuthTokenManager
    val base62Codec: Base62
    val authProps: AppAuthProperties

    fun Authentication.updateTokens(
        timestamp: LocalDateTime,
        clientInfo: ClientAuthInfo
    ): FreshHttpAuthorizationToken {
        val token = authTokenMgr.create(loginName, "", timestamp)

        createRefreshToken(timestamp, authProps.refreshTokenAliveDays)

        return token
    }

    /**
     * Creates a refresh token for this authentication object and updates relative fields.
     *
     * @throws IllegalStateException if [Authentication.loginName] is empty.
     */
    fun Authentication.createRefreshToken(timestamp: LocalDateTime, lifespanDays: Long) {
        if (loginName.isEmpty()) {
            throw IllegalStateException("LoginName must be specified for this operation.")
        }

        this.refreshToken = getSecureRandomBytes(Authentication.REFRESH_TOKEN_LENGTH_BYTES)
        this.refreshTokenIssuedAt = timestamp
        this.refreshTokenExpireAt = timestamp.plusDays(lifespanDays)
    }

    fun Authentication.validateRefreshToken(oldToken: ByteArray): Boolean {
        // Rare cases
        if (utcNow() > refreshTokenExpireAt) {
            return false
        }

        return refreshToken.contentEquals(oldToken)
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

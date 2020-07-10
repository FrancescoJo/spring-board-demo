/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.property

import java.util.concurrent.TimeUnit

/**
 * Application-wide properties set for authentication.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
interface AppAuthProperties {
    /**
     * Valid authentication token lifespan. It is recommended to remain this value 'short' to prevent
     * token abuse. Range of 30 minutes to 2 hours is commonly used.
     * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/access-token-lifetime/">Access Token Lifetime</a>
     */
    val authTokenAliveSecs: Int

    /**
     * Valid refresh token lifespan. Refresh token could be lived relatively long compare to authentication tokens.
     * Range of 1 months to 1 year is commonly used.
     *
     * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/access-token-lifetime/">Access Token Lifetime</a>
     */
    val refreshTokenAliveDays: Int

    /**
     * Issuer name of authentication tokens.
     */
    val tokenIssuer: String

    companion object {
        val DEFAULT_AUTH_TOKEN_ALIVE_SECS = TimeUnit.HOURS.toSeconds(2L).toInt()

        val DEFAULT_REFRESH_TOKEN_ALIVE_DAYS = TimeUnit.DAYS.toSeconds(180).toInt()
    }
}

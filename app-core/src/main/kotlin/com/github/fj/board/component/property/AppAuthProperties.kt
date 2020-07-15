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
     * RSA Key lifetime definition, which is used by JWT signing.
     * Shorter value increases security but consumes more CPU and memory, longer value takes counter effects.
     */
    val rsaKeyAliveHours: Long

    /**
     * Valid authentication token lifespan. It is recommended to remain this value 'short' to prevent
     * token abuse. Range of 30 minutes to 2 hours is commonly used.
     * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/access-token-lifetime/">Access Token Lifetime</a>
     */
    val authTokenAliveSecs: Long

    /**
     * Valid refresh token lifespan. Refresh token could be lived relatively long compare to authentication tokens.
     * Range of 1 months to 1 year is commonly used.
     *
     * @see <a href="https://www.oauth.com/oauth2-servers/access-tokens/access-token-lifetime/">Access Token Lifetime</a>
     */
    val refreshTokenAliveDays: Long

    /**
     * Issuer name of authentication tokens.
     */
    val tokenIssuer: String

    companion object {
        val DEFAULT_RSA_KEY_ALIVE_HOURS = TimeUnit.HOURS.toHours(2)

        val DEFAULT_AUTH_TOKEN_ALIVE_SECS = TimeUnit.HOURS.toSeconds(2L)

        val DEFAULT_REFRESH_TOKEN_ALIVE_DAYS = TimeUnit.DAYS.toDays(180)
    }
}

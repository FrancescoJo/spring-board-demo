/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
sealed class HttpAuthorizationToken(
    private val scheme: HttpAuthScheme,
    private val token: String
) : AbstractAuthenticationToken(null) {
    override fun getPrincipal(): HttpAuthScheme = scheme

    override fun getCredentials(): String = token

    companion object {
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization
        const val HEADER_NAME = "Authorization"
    }
}

/**
 * Already issued [HttpAuthorizationToken], or maybe a rogue token from client.
 */
data class MaybeHttpAuthorizationToken(
    private val scheme: HttpAuthScheme,
    private val token: String
) : HttpAuthorizationToken(scheme, token)

/**
 * Newly issued [HttpAuthorizationToken] to be delivered to client.
 */
data class FreshHttpAuthorizationToken(
    private val token: String,
    val expiration: LocalDateTime
) : HttpAuthorizationToken(HttpAuthScheme.TOKEN, token)

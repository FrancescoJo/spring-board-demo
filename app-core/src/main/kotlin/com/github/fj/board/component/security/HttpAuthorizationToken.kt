/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.security

import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
data class HttpAuthorizationToken(
    val scheme: HttpAuthScheme,
    val token: String
) : AbstractAuthenticationToken(null) {
    override fun getPrincipal(): HttpAuthScheme = scheme

    override fun getCredentials(): String = token

    companion object {
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}

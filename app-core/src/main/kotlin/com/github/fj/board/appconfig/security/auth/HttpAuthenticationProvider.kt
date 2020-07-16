/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.security.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.security.HttpAuthScheme
import com.github.fj.board.component.security.HttpAuthorizationToken
import com.github.fj.board.exception.client.AuthTokenException
import org.slf4j.Logger
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
internal class HttpAuthenticationProvider(
    // Intended to name as this way: this logger should be reused to keep same log name
    @SuppressWarnings("ConstructorParameterNaming")
    private val LOG: Logger,
    private val tokenMgr: AuthTokenManager
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication? {
        val httpAuthToken = authentication as? HttpAuthorizationToken

        if (httpAuthToken == null) {
            LOG.warn("Cannot understand given authentication object: {}", authentication)
            return null
        }

        return when (httpAuthToken.principal) {
            HttpAuthScheme.TOKEN -> getAuthentication(httpAuthToken)
            else                 ->
                throw UnsupportedOperationException("$this type of authentication is not supported.")
        }
    }

    private fun getAuthentication(authentication: HttpAuthorizationToken): Authentication? {
        val ourAuthentication = try {
            tokenMgr.validate(authentication)
        } catch (e: AuthTokenException) {
            throw AuthenticationCredentialsNotFoundException(e.message, e)
        }

        LOG.trace("Authentication object was found: {}", ourAuthentication::class.qualifiedName)
        // We don't need to set this object into SecurityContextHolder, because Spring will do it for us.
        return ourAuthentication
    }

    override fun supports(authentication: Class<*>?): Boolean =
        authentication?.let {
            HttpAuthorizationToken::class.java.isAssignableFrom(it)
        } ?: false
}

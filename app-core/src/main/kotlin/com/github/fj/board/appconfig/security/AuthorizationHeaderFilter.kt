/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.security

import com.github.fj.board.appconfig.SecurityConfig
import com.github.fj.board.component.security.HttpAuthScheme
import com.github.fj.board.component.security.HttpAuthorizationToken
import com.github.fj.board.component.security.MaybeHttpAuthorizationToken
import com.github.fj.lib.text.matchesIn
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import java.util.regex.Pattern
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This filter attempts to locate `Authorization` header in user request and parses it via
 * authorisation types and saves its result into [org.springframework.security.core.context.SecurityContextHolder].
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
internal class AuthorizationHeaderFilter(
    private val exclusions: List<RequestMatcher>
) : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as? HttpServletRequest
        val resp = response as? HttpServletResponse
        if (req == null || resp == null) {
            LOG.warn("Cannot accept requests other than HttpServletRequest.")
            chain.doFilter(request, response)
            return
        }

        if (exclusions.any { it.matches(request) }) {
            LOG.trace("This request does not seems requiring any authentication.")
            chain.doFilter(req, resp)
            return
        }

        findAuthorizationHeader(req)?.let {
            LOG.trace("HTTP Authorization header has been found with: ${it.principal} scheme")
            SecurityContextHolder.getContext().authentication = it
        }

        chain.doFilter(req, resp)
    }

    companion object {
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization
        private const val HEADER_AUTHORIZATION = "Authorization"

        private val AUTHORIZATION_SYNTAX = Pattern.compile("(?i)(Basic|Bearer|Token) [A-Za-z0-9.+_-]+")

        private val LOG = LoggerFactory.getLogger(SecurityConfig::class.java)

        private fun findAuthorizationHeader(req: HttpServletRequest): HttpAuthorizationToken? =
            req.getHeader(HEADER_AUTHORIZATION).let { h ->
                when {
                    h.isNullOrEmpty()                 ->
                        LOG.trace("{}: No {} header in the request.", req.requestURI, HEADER_AUTHORIZATION)
                    h.matchesIn(AUTHORIZATION_SYNTAX) -> return h.split(" ").let {
                        MaybeHttpAuthorizationToken(HttpAuthScheme.byTypeValue(it[0]), it[1])
                    }
                    else                              ->
                        LOG.trace(
                            "{}: {} header does not match the syntax: '{}'", req.requestURI, HEADER_AUTHORIZATION, h
                        )
                }
                return null
            }
    }
}

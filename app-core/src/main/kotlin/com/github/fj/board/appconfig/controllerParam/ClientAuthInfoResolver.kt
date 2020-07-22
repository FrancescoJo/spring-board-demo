/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.controllerParam

import com.github.fj.board.component.auth.impl.AuthenticationObjectImpl
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.server.NotImplementedException
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
internal class ClientAuthInfoResolver(
    private val authCheckBypassUris: List<RequestMatcher>
) : HandlerMethodArgumentResolver {
    override fun resolveArgument(
        param: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webReq: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): ClientAuthInfo? {
        val httpReq: HttpServletRequest = webReq.nativeRequest as? HttpServletRequest
            ?: throw NotImplementedException("HTTP Request processor has been changed.")

        val currentAuthentication = SecurityContextHolder.getContext()?.authentication ?: run {
            if (httpReq.isAuthRequired()) {
                throw UnauthenticatedException()
            } else {
                return@run null
            }
        }

        val authObject = currentAuthentication as? AuthenticationObjectImpl
            ?: run {
            if (httpReq.isAuthRequired()) {
                val errMsg = currentAuthentication?.let {
                    "Authentication kind is not supported for user '${it.name}'"
                } ?: "Authentication kind is not supported"

                throw NotImplementedException(errMsg)
            } else {
                return@run null
            }
        }

        return if (authObject == null) {
            null
        } else {
            ClientAuthInfo.create(authObject.name, httpReq)
        }
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == ClientAuthInfo::class.java

    private fun HttpServletRequest.isAuthRequired() = authCheckBypassUris.none { it.matches(this) }
}

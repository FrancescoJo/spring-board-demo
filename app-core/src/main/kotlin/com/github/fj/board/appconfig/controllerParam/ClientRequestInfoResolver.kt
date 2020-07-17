/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.controllerParam

import com.github.fj.board.component.auth.AuthenticationObjectImpl
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.exception.server.NotImplementedException
import com.github.fj.board.vo.auth.ClientRequestInfo
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
class ClientRequestInfoResolver : HandlerMethodArgumentResolver {
    override fun resolveArgument(
        param: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webReq: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val currentAuthentication =
            SecurityContextHolder.getContext()?.authentication ?: throw UnauthorisedException()

        val authObject = currentAuthentication as? AuthenticationObjectImpl
            ?: throw NotImplementedException("Authentication kind is not supported for user " +
                    "'${currentAuthentication.name}'")

        val httpReq: HttpServletRequest = webReq.nativeRequest as? HttpServletRequest
            ?: throw NotImplementedException("HTTP Request processor has been changed.")

        return ClientRequestInfo.create(authObject.name, httpReq)
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == ClientRequestInfo::class.java
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.auth.impl

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@Component
internal class ControllerClientAuthInfoDetectorImpl : ControllerClientAuthInfoDetector {
    override fun detectClientAuthInfo(): ClientAuthInfo? {
        val maybeAuthentication = SecurityContextHolder.getContext()?.authentication
        val maybeAuthObject = maybeAuthentication as? AuthenticationObjectImpl

        val maybeServletReqAttrs = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val maybeHttpReq = maybeServletReqAttrs?.request

        return if (maybeHttpReq == null || maybeAuthObject == null) {
            null
        } else {
            ClientAuthInfo.create(maybeAuthObject.name, maybeHttpReq)
        }
    }
}

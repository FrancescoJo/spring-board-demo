/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.slf4j.Logger
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
interface OptionalClientAuthInfoMixin {
    val authDetector: ControllerClientAuthInfoDetector

    fun HttpServletRequest.maybeClientAuthInfo(logger: Logger): ClientAuthInfo? =
        authDetector.detectClientAuthInfo().also {
            if (it == null) {
                val queryStr = queryString?.let { q -> "?$q" } ?: ""

                logger.debug("{} {}{}", method, requestURI, queryStr)
            } else {
                logger.debug("{}", it.requestLine)
            }
        }
}

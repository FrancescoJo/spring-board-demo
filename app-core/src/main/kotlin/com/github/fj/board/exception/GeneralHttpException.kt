/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException

/**
 * This class represents a general HTTP exception. However, it is discouraged to use only this
 * class for exception handling. Elaborate errors as much as possible.
 *
 * There is [HttpStatusCodeException], however its message creation mechanism is unconfigurable;
 * this class is designed for custom message schemes for various HTTP errors.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
open class GeneralHttpException private constructor(
    val httpStatus: HttpStatus,
    override val message: String = "",
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {
    companion object {
        fun create(httpStatus: HttpStatus, resourceName: String = "", cause: Throwable? = null) =
            if (resourceName.isEmpty()) {
                GeneralHttpException(httpStatus, httpStatus.reasonPhrase, cause)
            } else {
                GeneralHttpException(
                    httpStatus, httpStatus.reasonPhrase + ": $resourceName",
                    cause
                )
            }
    }
}

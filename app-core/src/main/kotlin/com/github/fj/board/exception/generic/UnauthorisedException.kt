/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.generic

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class UnauthorisedException(
    override val message: String = "You are not authorised.",
    override val cause: Throwable? = null
) : GeneralHttpException(STATUS, message, cause) {
    companion object {
        val STATUS = HttpStatus.FORBIDDEN
    }
}

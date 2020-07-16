/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.server

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
class NotImplementedException(
    override val message: String = "An unavailable operation: not implemented.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.NOT_IMPLEMENTED, message, cause)

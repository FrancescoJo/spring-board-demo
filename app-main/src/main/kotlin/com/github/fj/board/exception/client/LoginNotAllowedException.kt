/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class LoginNotAllowedException(
    override val message: String = "Login is not allowed.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.BAD_REQUEST, message, cause)

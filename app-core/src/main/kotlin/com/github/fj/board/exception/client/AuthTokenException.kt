/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 11 - Nov - 2018
 */
open class AuthTokenException(
    override val message: String = "Wrong authentication token.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.UNAUTHORIZED, message, cause)

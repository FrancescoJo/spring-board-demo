/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.auth

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * Use this exception if only logical path is passed beyond security checks.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
class LoginNameNotFoundException(
    override val message: String = "No matching loginName.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.NOT_FOUND, message, cause)

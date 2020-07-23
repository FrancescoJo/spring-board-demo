/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.user

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
class UserNotFoundException(
    override val message: String = "User not found.",
    override val cause: Throwable? = null
) : GeneralHttpException(STATUS, message, cause) {
    companion object {
        val STATUS = HttpStatus.NOT_FOUND
    }
}

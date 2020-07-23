/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.generic

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 * @see <a href="https://stackoverflow.com/questions/3297048/403-forbidden-vs-401-unauthorized-http-responses">403 vs 401</a>
 */
class UnauthenticatedException(
    override val message: String = "You are not authenticated.",
    override val cause: Throwable? = null
) : GeneralHttpException(STATUS, message, cause) {
    companion object {
        val STATUS = HttpStatus.UNAUTHORIZED
    }
}

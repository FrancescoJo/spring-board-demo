/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.auth

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2020
 */
class CannotWithdrawException(
    override val message: String = "Cannot process withdraw request.",
    override val cause: Throwable? = null
) : GeneralHttpException(STATUS, message, cause) {
    companion object {
        val STATUS = HttpStatus.FORBIDDEN
    }
}

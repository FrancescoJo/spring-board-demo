/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.reply

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class ReplyNotFoundException(
    override val message: String = "Reply is not found for given parameter.",
    override val cause: Throwable? = null
) : GeneralHttpException(STATUS, message, cause) {
    companion object {
        val STATUS = HttpStatus.NOT_FOUND
    }
}

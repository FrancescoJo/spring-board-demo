/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.reply

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class CannotCreateReplyException(
    override val message: String = "Cannot create a reply in target post.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.FORBIDDEN, message, cause) {
    companion object {
        val STATUS = HttpStatus.FORBIDDEN
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.post

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
class CannotDeletePostException(
    override val message: String = "Cannot delete post in target board.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.FORBIDDEN, message, cause) {
    companion object {
        val STATUS = HttpStatus.FORBIDDEN
    }
}

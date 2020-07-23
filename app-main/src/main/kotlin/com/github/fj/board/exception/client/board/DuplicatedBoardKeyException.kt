/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.board

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
class DuplicatedBoardKeyException(
    override val message: String = "Board key is already exist.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.CONFLICT, message, cause) {
    companion object {
        val STATUS = HttpStatus.CONFLICT
    }
}

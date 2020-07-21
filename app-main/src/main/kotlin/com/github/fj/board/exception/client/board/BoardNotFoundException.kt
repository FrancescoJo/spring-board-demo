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
class BoardNotFoundException(
    override val message: String = "Board key is not found.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.NOT_FOUND, message, cause)

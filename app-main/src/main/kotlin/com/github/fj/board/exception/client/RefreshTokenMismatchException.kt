/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
class RefreshTokenMismatchException(
    override val message: String = "Refresh token mismatch.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.FORBIDDEN, message, cause)

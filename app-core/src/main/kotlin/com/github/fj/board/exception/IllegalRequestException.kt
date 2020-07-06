/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception

import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
class IllegalRequestException(
    override val message: String = "",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.BAD_REQUEST, message, cause)

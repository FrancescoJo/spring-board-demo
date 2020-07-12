/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class DuplicatedLoginNameException(
    override val message: String = "Duplicated login name.",
    override val cause: Throwable? = null
) : GeneralHttpException(HttpStatus.CONFLICT, message, cause)

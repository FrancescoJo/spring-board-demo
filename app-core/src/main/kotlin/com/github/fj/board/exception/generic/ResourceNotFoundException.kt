/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.generic

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
class ResourceNotFoundException(
    resourceName: String,
    override val cause: Throwable? = null
) : GeneralHttpException(
    httpStatus = HttpStatus.NOT_FOUND,
    message = if (resourceName.isEmpty()) {
        "Resource not found."
    } else {
        "Resource '$resourceName' is not found."
    },
    cause = cause
)

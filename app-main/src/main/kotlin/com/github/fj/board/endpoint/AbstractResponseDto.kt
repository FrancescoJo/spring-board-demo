/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint

import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcNow

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class AbstractResponseDto<T>(val type: Type) {
    abstract val body: T

    val timestamp: Long = utcNow().utcEpochSecond()

    companion object {
        fun <T> ok(payload: T) = OkResponseDto(payload)

        fun error(message: String, cause: String = "") = ErrorResponseDto(
            ErrorResponseDto.Body(message, cause)
        )
    }

    enum class Type {
        OK,
        ERROR
    }
}

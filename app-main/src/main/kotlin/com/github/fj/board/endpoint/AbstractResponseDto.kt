/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint

import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcNow

/**
 * An generic envelop type of all responses.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
abstract class AbstractResponseDto<T>(
    @JsonPropertyDescription(DESC_TYPE)
    val type: Type
) {
    @get:JsonPropertyDescription(DESC_BODY)
    abstract val body: T

    @JsonPropertyDescription(DESC_TIMESTAMP)
    val timestamp: Long = utcNow().utcEpochSecond()

    companion object {
        const val DESC_TYPE = "Type of enclosing response. Either be 'OK' or 'ERROR'."
        const val DESC_TIMESTAMP = "UNIX epoch timestamp of request negotiation."
        const val DESC_BODY = "An actual payload of this response object."

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

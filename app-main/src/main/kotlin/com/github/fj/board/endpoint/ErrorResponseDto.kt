/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint

import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.github.fj.board.endpoint.ErrorResponseDto.Body

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
data class ErrorResponseDto(override val body: Body) : AbstractResponseDto<Body>(Type.ERROR) {
    data class Body(
        @JsonPropertyDescription(DESC_BODY_MESSAGE)
        val message: String,

        @JsonPropertyDescription(DESC_BODY_CAUSE)
        val cause: String
    )

    companion object {
        const val DESC_BODY_MESSAGE = "Detailed message of this error. Human readable hint text."
        const val DESC_BODY_CAUSE = "Cause of this error. Utilised for detecting reasons programmatically."
    }
}

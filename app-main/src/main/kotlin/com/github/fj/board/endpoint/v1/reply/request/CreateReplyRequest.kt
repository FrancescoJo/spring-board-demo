/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.github.fj.board.validation.UnicodeCharsLength

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class CreateReplyRequest(
    @get:UnicodeCharsLength(
        min = 1,
        message = "`content` must not be empty."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_CONTENT)
    val content: String
) {
    companion object {
        const val DESC_CONTENT = "Content of this post."
    }
}

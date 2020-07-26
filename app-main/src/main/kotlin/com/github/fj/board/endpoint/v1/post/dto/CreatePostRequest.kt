/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.validation.UnicodeCharsLength
import javax.validation.constraints.Size

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@JsonDeserialize
data class CreatePostRequest(
    @get:UnicodeCharsLength(
        min = 1,
        max = KEY_SIZE_MAX,
        message = "`title` must not be empty and less than $KEY_SIZE_MAX characters long."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_TITLE)
    val title: String,

    @get:UnicodeCharsLength(
        min = 1,
        message = "`content` must not be empty."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_CONTENT)
    val content: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_ATTACHMENTS)
    val attachments: List<CreateAttachmentRequest>
) {
    companion object {
        const val KEY_SIZE_MAX = 40

        const val DESC_TITLE = "Title of this post."
        const val DESC_CONTENT = "Content of this post."
        const val DESC_ATTACHMENTS = "Attachments of this post. Maybe null or empty."
    }
}

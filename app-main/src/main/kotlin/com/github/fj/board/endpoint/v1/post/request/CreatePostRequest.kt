/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.validation.UnicodeCharsLength

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@JsonDeserialize
data class CreatePostRequest(
    @JsonProperty
    @JsonPropertyDescription(DESC_MODE)
    val mode: PostMode,

    @get:UnicodeCharsLength(
        min = 1,
        max = TITLE_SIZE_MAX,
        message = "`title` must not be empty and less than $TITLE_SIZE_MAX characters long."
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
        const val TITLE_SIZE_MAX = 40

        const val DESC_MODE = "Mode of this post. ['frv': Free to reply, 'rr': Reply not allowed]."
        const val DESC_TITLE = "Title of this post."
        const val DESC_CONTENT = "Content of this post."
        const val DESC_ATTACHMENTS = "Attachments of this post. Maybe null or empty."
    }
}

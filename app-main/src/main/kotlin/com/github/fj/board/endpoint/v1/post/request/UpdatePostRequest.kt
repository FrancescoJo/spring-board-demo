/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.validation.UnicodeCharsLength
import com.github.fj.lib.text.REGEX_UUID
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
@JsonDeserialize
data class UpdatePostRequest(
    @get:Pattern(
        regexp = REGEX_UUID,
        message = "`accessId` must be in a UUID format."
    )
    @JsonProperty("accessId")
    @JsonPropertyDescription(DESC_ACCESS_ID)
    val accessId: String,

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyDescription(DESC_ATTACHMENTS)
    val attachments: List<UpdateAttachmentRequest>?
) {
    companion object {
        const val TITLE_SIZE_MAX = 40

        const val DESC_ACCESS_ID = "An UUID of target post to edit."
        const val DESC_MODE = "Mode of this post. ['frv': Free to reply, 'rr': Reply not allowed]."
        const val DESC_TITLE = "Title of  target post to edit."
        const val DESC_CONTENT = "Content of  target post to edit."
        const val DESC_ATTACHMENTS = "Attachments of target post. Maybe null or empty."
    }
}

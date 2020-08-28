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
import com.github.fj.board.service.post.PostEditingServiceMixin.Companion.MAXIMUM_ATTACHMENTS_PER_POST
import com.github.fj.board.validation.UnicodeCharsLength
import javax.validation.constraints.Size

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
@JsonDeserialize
data class UpdatePostRequest(
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
    @JsonPropertyDescription(DESC_CONTENTS)
    val contents: String,

    @get:Size(max = MAXIMUM_ATTACHMENTS_PER_POST)
    @JsonProperty
    @JsonPropertyDescription(DESC_ATTACHMENTS)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val attachments: List<UpdateAttachmentRequest>
) {
    companion object {
        const val TITLE_SIZE_MAX = 40

        const val DESC_MODE = "Mode of this post. Read link:#common-types-postMode[`PostMode`] " +
                "for more details."
        const val DESC_TITLE = "Title of  target post to edit."
        const val DESC_CONTENTS = "Contents of target post to edit."
        const val DESC_ATTACHMENTS = "Attachments of target post. Maybe null or empty."
    }
}

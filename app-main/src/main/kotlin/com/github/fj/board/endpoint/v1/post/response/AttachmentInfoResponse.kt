/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.vo.post.AttachmentInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@JsonSerialize
data class AttachmentInfoResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_POST_ID)
    val postId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_ATTACHMENT_ID)
    val accessId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_URI)
    val uri: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_MIME_TYPE)
    val mimeType: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_NAME)
    val name: String
) {
    companion object {
        const val DESC_POST_ID = "An UUID of post which contains this attachment."
        const val DESC_ATTACHMENT_ID = "An UUID of this attachment."
        const val DESC_URI = "URI of this attachment."
        const val DESC_MIME_TYPE = "MIME type of this attachment."
        const val DESC_NAME = "Name of this attachment to distinguish."

        fun from(src: AttachmentInfo) = AttachmentInfoResponse(
            postId = src.parentPostId.toString(),
            accessId = src.accessId.toString(),
            uri = src.uri,
            mimeType = src.mimeType,
            name = src.name
        )
    }
}

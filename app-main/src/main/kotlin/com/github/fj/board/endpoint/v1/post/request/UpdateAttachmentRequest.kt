/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
@JsonDeserialize(using = UpdateAttachmentsDeserialiser::class)
data class UpdateAttachmentRequest(
    @JsonProperty
    @JsonPropertyDescription(DESC_MODE)
    val mode: AttachmentModeRequest,

    @JsonProperty
    @JsonPropertyDescription(DESC_PAYLOAD)
    val payload: Any
) {
    companion object {
        const val DESC_MODE = "Update mode: ['+': Create more attachment, '-': Delete existing attachment]."
        const val DESC_PAYLOAD = "Description of this attachment. Type is determined by `mode`. " +
                "`CreateAttachmentRequest` for creation, " +
                "`DeleteAttachmentRequest` for deletion."
    }
}

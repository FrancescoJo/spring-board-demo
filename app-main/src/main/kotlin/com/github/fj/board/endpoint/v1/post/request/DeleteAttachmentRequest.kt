/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
@JsonDeserialize
data class DeleteAttachmentRequest @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @JsonProperty("accessId")
    @JsonPropertyDescription(DESC_ACCESS_ID)
    val accessId: String
) {
    companion object {
        const val DESC_ACCESS_ID = "An UUID of this attachment."
    }
}

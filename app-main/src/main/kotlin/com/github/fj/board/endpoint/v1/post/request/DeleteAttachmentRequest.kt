/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.lib.text.REGEX_UUID
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
@JsonDeserialize
data class DeleteAttachmentRequest @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @get:Pattern(
        regexp = REGEX_UUID,
        message = "`accessId` must be in a UUID format."
    )
    @JsonProperty("accessId")
    @JsonPropertyDescription(DESC_ACCESS_ID)
    val accessId: String
) {
    companion object {
        const val DESC_ACCESS_ID = "An UUID of this attachment."
    }
}

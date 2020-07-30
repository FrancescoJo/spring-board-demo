/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@JsonDeserialize
data class CreateAttachmentRequest @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @get:Pattern(
        /* https://tools.ietf.org/html/rfc3986#appendix-B */
        regexp = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?",
        message = "`uri` must be in form of RFC 3986 standard."
    )
    @JsonProperty("uri")
    @JsonPropertyDescription(DESC_URI)
    val uri: String,

    @JsonProperty("mimeType")
    @JsonPropertyDescription(DESC_MIME_TYPE)
    val mimeType: String,

    @JsonProperty("name")
    @JsonPropertyDescription(DESC_NAME)
    val name: String
) {
    companion object {
        const val DESC_URI = "URI of this attachment. Must conform RFC 3986 standard."
        const val DESC_MIME_TYPE = "MIME type of this attachment. Only well-known MIME types are accepted."
        const val DESC_NAME = "Name of this attachment to distinguish. Must not be empty."
    }
}

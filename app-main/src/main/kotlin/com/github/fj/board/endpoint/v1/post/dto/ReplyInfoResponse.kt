/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.auth.PlatformType
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@JsonSerialize
data class ReplyInfoResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_POST_ID)
    val postId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_REPLY_ID)
    val replyId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_WRITER_NICKNAME)
    val writerNickname: String?,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_MODIFIED_DATE)
    val lastModifiedDate: LocalDateTime,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_MODIFIED_IP)
    val lastModifiedIp: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_MODIFIED_PLATFORM_TYPE)
    val lastModifiedPlatformType: PlatformType = PlatformType.UNDEFINED,

    @JsonProperty
    @JsonPropertyDescription(DESC_NUMBER)
    val number: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_CONTENTS)
    val contents: String
) {
    companion object {
        const val DESC_POST_ID = "An UUID of post which contains this reply."
        const val DESC_REPLY_ID = "An UUID of this reply."
        const val DESC_WRITER_NICKNAME = "Writer's nickname of this reply. Maybe null depends on board mode."
        const val DESC_LAST_MODIFIED_DATE = "The very last date when this reply's writer finished their edits."
        const val DESC_LAST_MODIFIED_IP = "The very last IP address of client " +
                "when this reply's writer finished their edits."
        const val DESC_LAST_MODIFIED_PLATFORM_TYPE = "The very last platform type['a': Android, 'i': iOS, 'w': Web] " +
                "of client when this reply's writer finished their edits. ."
        const val DESC_NUMBER = "Number of this post in board."
        const val DESC_CONTENTS = "Content of this post."
    }
}

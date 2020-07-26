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
data class PostInfoDetailedResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_BOARD_ID)
    val boardId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_POST_ID)
    val postId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_POST_NUMBER)
    val postNumber: Long,

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
    @JsonPropertyDescription(DESC_EDITED)
    val isEdited: Boolean,

    @JsonProperty
    @JsonPropertyDescription(DESC_NUMBER)
    val number: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_TITLE)
    val title: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_CONTENTS)
    val contents: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_VIEW_COUNT)
    val viewCount: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_ATTACHMENTS)
    val attachments: List<AttachmentInfoResponse>,

    @JsonProperty
    @JsonPropertyDescription(DESC_REPLY_COUNT)
    val replyCount: Long
) {
    companion object {
        const val DESC_BOARD_ID = "An UUID of board which contains this post."
        const val DESC_POST_ID = "An UUID of this post."
        const val DESC_POST_NUMBER = "A sequence number of this post in board."
        const val DESC_WRITER_NICKNAME = "Writer's nickname of this post. Maybe null depends on board mode."
        const val DESC_LAST_MODIFIED_DATE = "The very last date when this post's writer finished their edits."
        const val DESC_LAST_MODIFIED_IP = "The very last IP address of client " +
                "when this post's writer finished their edits."
        const val DESC_LAST_MODIFIED_PLATFORM_TYPE = "The very last platform type['a': Android, 'i': iOS, 'w': Web] " +
                "of client when this post's writer finished their edits. ."
        const val DESC_EDITED = "Boolean flag to determine this post is whether original or not."
        const val DESC_NUMBER = "Number of this post in board."
        const val DESC_TITLE = "Title of this post."
        const val DESC_CONTENTS = "Content of this post."
        const val DESC_VIEW_COUNT = "View count of this post."
        const val DESC_ATTACHMENTS = "Attachments of this post."
        const val DESC_REPLY_COUNT = "Total reply counts of this post."
    }
}

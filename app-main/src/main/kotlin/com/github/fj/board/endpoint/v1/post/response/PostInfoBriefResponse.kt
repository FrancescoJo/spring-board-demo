/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.vo.post.PostBriefInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Jul - 2020
 */
@JsonSerialize
data class PostInfoBriefResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_BOARD_ID)
    val boardId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_POST_ID)
    val postId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_POST_MODE)
    val postMode: PostMode,

    @JsonProperty
    @JsonPropertyDescription(DESC_POST_NUMBER)
    val postNumber: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_WRITER_NICKNAME)
    val writerNickname: String?,

    @JsonProperty
    @JsonPropertyDescription(DESC_WRITER_LOGIN_NAME)
    val writerLoginName: String?,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_MODIFIED_DATE)
    val lastModifiedDate: LocalDateTime,

    @JsonProperty
    @JsonPropertyDescription(DESC_TITLE)
    val title: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_VIEW_COUNT)
    val viewCount: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_REPLY_COUNT)
    val replyCount: Long
) {
    companion object {
        const val DESC_BOARD_ID = "An UUID of board which contains this post."
        const val DESC_POST_ID = "An UUID of this post."
        const val DESC_POST_MODE = "Mode of this post. Read link:#common-types-postMode[`PostMode`] " +
                "for more details."
        const val DESC_POST_NUMBER = "A sequence number of this post in board."
        const val DESC_WRITER_NICKNAME = "Writer's nickname of this post. Maybe null depends on board mode."
        const val DESC_WRITER_LOGIN_NAME = "Writer's login name of this post. Maybe null depends on board mode."
        const val DESC_LAST_MODIFIED_DATE = "The very last date when this post's writer finished their edits."
        const val DESC_TITLE = "Title of this post."
        const val DESC_VIEW_COUNT = "View count of this post."
        const val DESC_REPLY_COUNT = "Total reply count of this post."

        fun from(src: PostBriefInfo) = PostInfoBriefResponse(
            boardId = src.boardId.toString(),
            postId = src.accessId.toString(),
            postMode = src.mode,
            postNumber = src.number,
            writerNickname = src.writerNickname,
            writerLoginName = src.writerLoginName,
            lastModifiedDate = src.lastModifiedDate,
            title = src.title,
            viewCount = src.viewedCount,
            replyCount = src.replyCount
        )
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.board.Status
import com.github.fj.board.vo.board.BoardInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@JsonSerialize
data class BoardInfoResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_ACCESS_ID)
    val accessId: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_STATUS)
    val status: Status,

    @JsonProperty
    @JsonPropertyDescription(DESC_KEY)
    val key: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_NAME)
    val name: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_DESCRIPTION)
    val description: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_POSTS_COUNT)
    val postsCount: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_CREATED_DATE)
    val createdDate: LocalDateTime,

    @JsonProperty
    @JsonPropertyDescription(DESC_MODIFIED_DATE)
    val modifiedDate: LocalDateTime,

    @JsonProperty
    @JsonPropertyDescription(DESC_CREATOR_LOGIN_NAME)
    val creatorLoginName: String
) {
    companion object {
        const val DESC_ACCESS_ID = "An UUID of this board."
        const val DESC_STATUS = "Status of this board. ['n': Normal, 'r': Read only, 'x': Archived, '-': Closed.]"
        const val DESC_KEY = "A distinct, human-friendly unique string to identify a board."
        const val DESC_NAME = "A display name of this board."
        const val DESC_DESCRIPTION = "Brief description of this board."
        const val DESC_POSTS_COUNT = "Number of approximated total posts of this board."
        const val DESC_CREATED_DATE = "The very first date when this board is created."
        const val DESC_MODIFIED_DATE =
            "A recent date that closest to any point of activity on this board, but may not accurate."
        const val DESC_CREATOR_LOGIN_NAME = "A login name of owner of this board."

        fun from(src: BoardInfo) = BoardInfoResponse(
            accessId = src.accessId.toString(),
            status = src.status,
            key = src.key,
            name = src.name,
            description = src.description,
            postsCount = src.postsCount,
            createdDate = src.createdDate,
            modifiedDate = src.modifiedDate,
            creatorLoginName = src.creator.loginName
        )
    }
}

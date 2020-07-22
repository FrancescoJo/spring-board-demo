/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.board.Access
import com.github.fj.board.vo.board.BoardInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@JsonSerialize
data class BoardInfoListResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_PUBLIC)
    val public: List<BoardInfoResponse>,

    @JsonProperty
    @JsonPropertyDescription(DESC_MEMBERS_ONLY)
    val membersOnly: List<BoardInfoResponse>
) {
    companion object {
        const val DESC_PUBLIC = "List of 'public' boards."
        const val DESC_MEMBERS_ONLY = "List of 'private' boards. Member access only."

        fun from(src: Map<Access, List<BoardInfo>>) = BoardInfoListResponse(
            public = src.listOf(Access.PUBLIC),
            membersOnly = src.listOf(Access.MEMBERS_ONLY)
        )

        private fun Map<Access, List<BoardInfo>>.listOf(access: Access): List<BoardInfoResponse> =
            get(access)?.map { BoardInfoResponse.from(it) } ?: emptyList()
    }
}

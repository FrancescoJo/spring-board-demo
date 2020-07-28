/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.vo.board.BoardInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@JsonSerialize
data class BoardInfoListResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_BOARDS)
    val boards: List<BoardInfoResponse>
) {
    companion object {
        const val DESC_BOARDS = "List of available boards. Contains multiple `BoardInfoResponse`."

        fun from(src: List<BoardInfo>) = BoardInfoListResponse(
            boards = src.map { BoardInfoResponse.from(it) }
        )
    }
}

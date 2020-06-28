/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Jun - 2020
 */
@JsonSerialize
data class BoardsResponse(
    val boards : List<BoardResponse>
)

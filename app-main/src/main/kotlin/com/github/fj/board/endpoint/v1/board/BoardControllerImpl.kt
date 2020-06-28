/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.endpoint.v1.board.dto.BoardResponse
import com.github.fj.board.endpoint.v1.board.dto.BoardsResponse
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Jun - 2020
 */
@RestController
class BoardControllerImpl : BoardController {
    override fun getBoards(): BoardsResponse {
        return BoardsResponse(
            boards = listOf(
                BoardResponse(
                    name = "Hello world"
                )
            )
        )
    }
}

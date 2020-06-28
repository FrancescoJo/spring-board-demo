/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardsResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Jun - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface BoardController {
    @RequestMapping(
        path = [ApiPaths.BOARDS],
        method = [RequestMethod.GET]
    )
    fun getBoards(): BoardsResponse
}

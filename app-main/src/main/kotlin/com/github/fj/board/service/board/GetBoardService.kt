/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.endpoint.v1.board.dto.BoardsSortBy
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortOrderBy
import com.github.fj.board.persistence.model.board.Access
import com.github.fj.board.persistence.model.board.Status
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
interface GetBoardService : BoardAccessMixin {
    fun getOne(accessId: String, clientInfo: ClientAuthInfo): BoardInfo

    fun getList(
        sortBy: BoardsSortBy,
        orderBy: BoardsSortOrderBy,
        clientInfo: ClientAuthInfo
    ): Map<Access, List<BoardInfo>>
}

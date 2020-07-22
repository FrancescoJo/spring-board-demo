/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.endpoint.v1.board.dto.BoardsSortBy
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortOrderBy
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.Access
import com.github.fj.board.persistence.model.board.Status
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@Service
internal class GetBoardServiceImpl(
    override val boardRepo: BoardRepository
) : GetBoardService {
    @Transactional
    override fun getOne(accessId: UUID, clientInfo: ClientAuthInfo?): BoardInfo {
        val board = accessId.getBoard()

        if (clientInfo == null && board.access != Access.PUBLIC) {
            // We want that such user cannot recognise whether it is even exist or not
            throw BoardNotFoundException()
        }

        return BoardInfo.from(board)
    }

    override fun getList(
        sortBy: BoardsSortBy,
        orderBy: BoardsSortOrderBy,
        clientInfo: ClientAuthInfo?
    ): Map<Access, List<BoardInfo>> {
        TODO("Not yet implemented")
    }
}

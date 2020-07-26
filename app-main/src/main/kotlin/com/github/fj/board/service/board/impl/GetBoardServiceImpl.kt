/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.endpoint.v1.board.dto.BoardsSortBy
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortOrderBy
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.*
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

        if (clientInfo == null && board.access != BoardAccess.PUBLIC) {
            // We want that such user cannot recognise whether it is even exist or not
            throw BoardNotFoundException()
        }

        return BoardInfo.from(board, board.getPostsCount())
    }

    @Transactional
    override fun getList(
        sortBy: BoardsSortBy,
        orderBy: BoardsSortOrderBy,
        clientInfo: ClientAuthInfo?
    ): List<BoardInfo> {
        val boards = if (clientInfo == null) {
            boardRepo.findAllByAccess(BoardAccess.PUBLIC, sortBy.toPropertyName(), orderBy.toSortDirection())
        } else {
            boardRepo.findAll(NormalBoardSearchSpec(), Sort.by(orderBy.toSortDirection(), sortBy.toPropertyName()))
        }

        return boards.map { BoardInfo.from(it, it.getPostsCount()) }
    }
}

private class NormalBoardSearchSpec : Specification<Board> {
    override fun toPredicate(root: Root<Board>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? =
        ArrayList<Predicate>().apply {
            add(criteriaBuilder.`in`(root.get<BoardStatus>("status")).apply { value(BoardStatus.NORMAL) })
        }.mergeBy(criteriaBuilder)

    private fun List<Predicate>.mergeBy(cb: CriteriaBuilder): Predicate? = cb.and(*this.toTypedArray())
}
/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.board.BoardsSortBy
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@Service
internal class GetBoardServiceImpl(
    override val boardRepo: BoardRepository
) : GetBoardService {
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun getOne(accessId: UUID, clientInfo: ClientAuthInfo?): BoardInfo {
        val board = accessId.getBoard().also {
            it.checkAccessibleFor(clientInfo)
        }

        return BoardInfo.from(board, board.getPostsCount())
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun getList(
        sortBy: BoardsSortBy,
        sortDirection: Sort.Direction,
        clientInfo: ClientAuthInfo?
    ): List<BoardInfo> {
        val boards = if (clientInfo == null) {
            boardRepo.findAllByAccess(BoardAccess.PUBLIC, sortBy.toPropertyName(), sortDirection)
        } else {
            boardRepo.findAll(NormalBoardSearchSpec(), Sort.by(sortDirection, sortBy.toPropertyName()))
        }

        return boards.map { BoardInfo.from(it, it.getPostsCount()) }
    }
}

private class NormalBoardSearchSpec : Specification<Board> {
    override fun toPredicate(root: Root<Board>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? =
        ArrayList<Predicate>().apply {
            add(criteriaBuilder.`in`(root.get<BoardStatus>("status")).apply { value(BoardStatus.NORMAL) })
        }.mergeBy(criteriaBuilder)

    private fun List<Predicate>.mergeBy(cb: CriteriaBuilder): Predicate? {
        var p: Predicate? = null
        forEach { p = cb.and(it) }
        return p
    }
}

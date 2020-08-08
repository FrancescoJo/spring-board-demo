/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.board

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardAccess
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Jul - 2020
 */
interface BoardRepositoryExtension {
    fun findAllByAccess(
        access: BoardAccess,
        orderByProperty: String = "",
        sortDirection: Sort.Direction
    ): List<Board>
}

@Repository
internal class BoardRepositoryExtensionImpl : BoardRepositoryExtension {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findAllByAccess(
        access: BoardAccess,
        orderByProperty: String,
        sortDirection: Sort.Direction
    ): List<Board> = em.createQuery(
        """
            SELECT b
            FROM Board b
            WHERE b.access = :access
              AND b.status <> com.github.fj.board.persistence.model.board.BoardStatus.CLOSED
            ORDER BY $orderByProperty $sortDirection
        """.trimIndent(),
        Board::class.java
    )
        .setParameter("access", access)
        .resultList
}

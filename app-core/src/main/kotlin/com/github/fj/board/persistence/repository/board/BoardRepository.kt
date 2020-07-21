/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.board

import com.github.fj.board.persistence.entity.board.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
interface BoardRepository : JpaRepository<Board, Long> {
    @Query(
        """
        SELECT b
        FROM Board b
        WHERE b.key = ?1
          AND b.status <> com.github.fj.board.persistence.model.board.Status.CLOSED
    """
    )
    fun findByKey(key: String): Board?

    @Query(
        """
        SELECT b
        FROM Board b
        WHERE b.accessId = ?1
          AND b.status <> com.github.fj.board.persistence.model.board.Status.CLOSED
    """
    )
    fun findByAccessId(accessId: UUID): Board?
}

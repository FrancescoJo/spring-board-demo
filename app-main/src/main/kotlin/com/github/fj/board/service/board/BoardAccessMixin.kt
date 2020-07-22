/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.repository.board.BoardRepository
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
interface BoardAccessMixin {
    val boardRepo: BoardRepository

    /**
     * @return [Board] of given [UUID] if found. `null` otherwise.
     */
    fun UUID.findBoard(): Board? = boardRepo.findByAccessId(this)

    /**
     * @return [Board] of given [UUID] if found.
     * @throws BoardNotFoundException if there is no board with given [UUID].
     */
    @Throws(BoardNotFoundException::class)
    fun UUID.getBoard(): Board = findBoard() ?: throw BoardNotFoundException()
}

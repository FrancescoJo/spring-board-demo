/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardsSortBy
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

    fun Board.checkAccessibleFor(auth: ClientAuthInfo?) {
        if (auth == null && access != BoardAccess.PUBLIC) {
            // We want that such user cannot recognise whether it is even exist or not
            throw BoardNotFoundException()
        }

        if (status == BoardStatus.CLOSED) {
            // We want that such user cannot recognise whether it is even exist or not
            throw BoardNotFoundException()
        }
    }

    fun Board.getPostsCount(): Long = boardRepo.getPostsCountById(this)

    fun BoardsSortBy.toPropertyName() = when (this) {
        BoardsSortBy.KEY                -> "key"
        BoardsSortBy.NAME               -> "name"
        BoardsSortBy.POSTS_COUNT        -> "postsCount"
        BoardsSortBy.CREATED_DATE       -> "createdDate"
        BoardsSortBy.LAST_MODIFIED_DATE -> "modifiedDate"
    }
}

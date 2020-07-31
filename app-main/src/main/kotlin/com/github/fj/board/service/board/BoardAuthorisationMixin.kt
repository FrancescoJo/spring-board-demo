/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotCreatePostException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
interface BoardAuthorisationMixin : BoardAccessMixin {
    /**
     * Checks given [User] has authority on given [Board].
     */
    @Throws(UnauthorisedException::class)
    fun User.assertAuthorityOf(board: Board) {
        if (this.id != board.creator.id) {
            throw UnauthorisedException()
        }
    }

    fun Board.isWritableOrThrowFor(user: User): Boolean {
        when {
            status == BoardStatus.CLOSED   -> throw BoardNotFoundException()
            status == BoardStatus.ARCHIVED -> throw CannotCreatePostException()
            mode == BoardMode.READ_ONLY    -> throw CannotCreatePostException()
        }

        return true
    }
}

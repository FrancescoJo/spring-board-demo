/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.user.User

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
}
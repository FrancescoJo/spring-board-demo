/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
interface DeletePostService : UserServiceMixin, BoardAuthorisationMixin, PostAccessMixin {
    fun delete(boardId: UUID, postId: UUID, clientInfo: ClientAuthInfo): Boolean
}

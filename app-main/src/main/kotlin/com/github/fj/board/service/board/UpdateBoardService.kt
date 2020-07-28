/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.endpoint.v1.board.request.UpdateBoardRequest
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
interface UpdateBoardService : UserServiceMixin, BoardAuthorisationMixin {
    fun update(accessId: UUID, req: UpdateBoardRequest, clientInfo: ClientAuthInfo): BoardInfo
}

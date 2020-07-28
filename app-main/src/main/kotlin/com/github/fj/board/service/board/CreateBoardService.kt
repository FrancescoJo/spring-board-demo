/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.endpoint.v1.board.request.CreateBoardRequest
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
interface CreateBoardService : UserServiceMixin {
    fun create(req: CreateBoardRequest, clientInfo: ClientAuthInfo): BoardInfo
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user

import com.github.fj.board.endpoint.v1.user.request.UpdateUserRequest
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.user.UserInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
interface UpdateUserService : UserServiceMixin {
    fun update(nickname: String, req: UpdateUserRequest, clientInfo: ClientAuthInfo) : UserInfo
}

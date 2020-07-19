/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user

import com.github.fj.board.endpoint.v1.user.dto.CreateUserRequest
import com.github.fj.board.vo.auth.ClientAuthInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
interface CreateUserService {
    fun create(req: CreateUserRequest, clientInfo: ClientAuthInfo)
}

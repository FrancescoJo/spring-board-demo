/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.endpoint.v1.auth.request.AuthenticationRequest
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientAuthInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
interface SignUpService : AuthenticationServiceMixin, PasswordServiceMixin {
    fun signUp(req: AuthenticationRequest, clientInfo: ClientAuthInfo): AuthenticationResult
}

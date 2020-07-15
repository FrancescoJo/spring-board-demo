/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.vo.auth.AuthenticationResult
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
interface SignInService {
    fun signIn(req: AuthenticationRequest, httpReq: HttpServletRequest): AuthenticationResult
}

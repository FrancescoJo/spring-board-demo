/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.vo.auth.SignUpResult
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
interface SignUpService {
    fun signUp(req: SignUpRequest, httpReq: HttpServletRequest): SignUpResult
}

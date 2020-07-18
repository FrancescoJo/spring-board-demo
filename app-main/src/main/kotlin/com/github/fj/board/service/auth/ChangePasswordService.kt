/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.endpoint.v1.auth.dto.ChangePasswordRequest
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.board.vo.auth.ClientRequestInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
interface ChangePasswordService : PasswordServiceMixin, AuthenticationServiceMixin {
    fun changePassword(
        req: ChangePasswordRequest,
        clientInfo: ClientRequestInfo,
        timestamp: LocalDateTime
    ): AuthenticationResult
}

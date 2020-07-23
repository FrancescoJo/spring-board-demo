/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client.auth

import com.github.fj.board.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * This exception obscures detailed reason for every login failures, for security reasons. For example, login attempts
 * will fail for various reasons, especially receiving nonexistent `loginName` from any peer.
 *
 * We don't need to expose honest cause of this failure in this case. Since peer are not always genuine clients
 * but attackers, explaining detailed reasons would be a valuable hint to them.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class LoginNotAllowedException(
    override val message: String = "Login is not allowed.",
    override val cause: Throwable? = null
) : GeneralHttpException(STATUS, message, cause) {
    companion object {
        val STATUS = HttpStatus.BAD_REQUEST
    }
}

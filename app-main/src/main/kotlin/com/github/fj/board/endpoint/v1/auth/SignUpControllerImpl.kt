/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.endpoint.v1.auth.dto.SignUpResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@RestController
class SignUpControllerImpl : SignUpController {
    override fun signUp(req: SignUpRequest, httpServletReq: HttpServletRequest): SignUpResponse {
        LOG.debug("Signup request: {}", req)

        TODO("Not yet implemented")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignUpController::class.java)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.endpoint.v1.auth.dto.SignUpResponse
import com.github.fj.board.service.auth.SignUpService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface SignUpController {
    @RequestMapping(
        path = [ApiPaths.AUTH],
        method = [RequestMethod.POST]
    )
    fun signUp(@Valid @RequestBody req: SignUpRequest, httpReq: HttpServletRequest): SignUpResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@RestController
internal class SignUpControllerImpl(
    private val svc: SignUpService
): SignUpController {
    override fun signUp(req: SignUpRequest, httpReq: HttpServletRequest): SignUpResponse {
        LOG.debug("Signup request: {}", req)

        val result = svc.signUp(req, httpReq)

        return SignUpResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignUpController::class.java)
    }
}

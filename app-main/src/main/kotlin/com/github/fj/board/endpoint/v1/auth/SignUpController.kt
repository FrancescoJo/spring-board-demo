/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.request.AuthenticationRequest
import com.github.fj.board.endpoint.v1.auth.response.AuthenticationResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.persistence.model.auth.PlatformType
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
        path = [ApiPaths.ACCOUNT],
        method = [RequestMethod.POST]
    )
    fun signUp(@Valid @RequestBody req: AuthenticationRequest, httpReq: HttpServletRequest): AuthenticationResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@RestController
internal class SignUpControllerImpl(
    private val svc: SignUpService
) : SignUpController {
    override fun signUp(req: AuthenticationRequest, httpReq: HttpServletRequest): AuthenticationResponse {
        LOG.debug("{} {}: {}", httpReq.method, httpReq.requestURI, req)

        if (req.platformType == PlatformType.UNDEFINED) {
            throw IllegalRequestException("`platformType` is not specified.")
        }

        val clientInfo = req.createClientAuthInfoBy(httpReq)
        val result = svc.signUp(req, clientInfo)

        return AuthenticationResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignUpController::class.java)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationResponse
import com.github.fj.board.endpoint.v1.auth.dto.RefreshTokenRequest
import com.github.fj.board.service.auth.RefreshAccessTokenService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface RefreshAccessTokenController {
    @RequestMapping(
        path = [ApiPaths.TOKEN],
        method = [RequestMethod.PATCH]
    )
    fun refresh(
        tokenAuth: Authentication,
        @Valid @RequestBody req: RefreshTokenRequest,
        httpReq: HttpServletRequest
    ): AuthenticationResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@RestController
internal class RefreshAccessTokenControllerImpl(
    private val svc: RefreshAccessTokenService
) : RefreshAccessTokenController {
    override fun refresh(
        tokenAuth: Authentication,
        req: RefreshTokenRequest,
        httpReq: HttpServletRequest
    ): AuthenticationResponse {
        TODO("Not yet implemented")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RefreshAccessTokenController::class.java)
    }
}

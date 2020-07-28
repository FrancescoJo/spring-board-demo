/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.request.ChangePasswordRequest
import com.github.fj.board.endpoint.v1.auth.response.AuthenticationResponse
import com.github.fj.board.service.auth.ChangePasswordService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface ChangePasswordController {
    @RequestMapping(
        path = [ApiPaths.PASSWORD],
        method = [RequestMethod.PATCH]
    )
    fun refresh(@Valid @RequestBody req: ChangePasswordRequest, clientInfo: ClientAuthInfo): AuthenticationResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@RestController
internal class ChangePasswordControllerImpl(
    private val svc: ChangePasswordService
) : ChangePasswordController {
    override fun refresh(req: ChangePasswordRequest, clientInfo: ClientAuthInfo): AuthenticationResponse {
        LOG.debug("{}: {}", clientInfo.requestLine, req)

        val result = svc.changePassword(req, clientInfo, utcNow())

        return AuthenticationResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RefreshAccessTokenController::class.java)
    }
}

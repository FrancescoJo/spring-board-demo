/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.ChangePasswordRequest
import com.github.fj.board.service.auth.ChangePasswordService
import com.github.fj.board.vo.auth.ClientRequestInfo
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
    fun refresh(@Valid @RequestBody req: ChangePasswordRequest, clientInfo: ClientRequestInfo)
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
@RestController
internal class ChangePasswordControllerImpl(
    private val svc: ChangePasswordService
) : ChangePasswordController {
    override fun refresh(req: ChangePasswordRequest, clientInfo: ClientRequestInfo) {
        LOG.debug("{}: {}", clientInfo.requestUri, req)

        svc.changePassword(req, clientInfo)

        return
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RefreshAccessTokenController::class.java)
    }
}

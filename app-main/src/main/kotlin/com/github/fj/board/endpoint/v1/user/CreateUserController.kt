/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.dto.CreateUserRequest
import com.github.fj.board.endpoint.v1.user.dto.CreateUserResponse
import com.github.fj.board.service.user.CreateUserService
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
 * @since 12 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface CreateUserController {
    @RequestMapping(
        path = [ApiPaths.USER],
        method = [RequestMethod.POST]
    )
    fun create(@Valid @RequestBody req: CreateUserRequest, clientInfo: ClientRequestInfo): CreateUserResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@RestController
internal class CreateUserControllerImpl(
    private val svc: CreateUserService
) : CreateUserController {
    override fun create(req: CreateUserRequest, clientInfo: ClientRequestInfo): CreateUserResponse {
        LOG.debug("{}: {}", clientInfo.requestUri, req)

        svc.create(req, clientInfo)

        return CreateUserResponse(req.nickname, req.email)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CreateUserController::class.java)
    }
}

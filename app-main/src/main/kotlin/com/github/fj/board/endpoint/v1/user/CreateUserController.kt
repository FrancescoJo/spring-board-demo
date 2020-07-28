/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.request.CreateUserRequest
import com.github.fj.board.endpoint.v1.user.response.UserInfoResponse
import com.github.fj.board.service.user.CreateUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
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
    fun create(@Valid @RequestBody req: CreateUserRequest, clientInfo: ClientAuthInfo): UserInfoResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@RestController
internal class CreateUserControllerImpl(
    private val svc: CreateUserService
) : CreateUserController {
    override fun create(req: CreateUserRequest, clientInfo: ClientAuthInfo): UserInfoResponse {
        LOG.debug("{}: {}", clientInfo.requestLine, req)

        val result = svc.create(req, clientInfo)

        return UserInfoResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CreateUserController::class.java)
    }
}

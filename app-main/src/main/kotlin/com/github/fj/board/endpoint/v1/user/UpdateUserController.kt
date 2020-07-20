/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.dto.UpdateUserRequest
import com.github.fj.board.endpoint.v1.user.dto.UserInfoResponse
import com.github.fj.board.service.user.UpdateUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface UpdateUserController {
    @RequestMapping(
        path = [ApiPaths.USER_NICKNAME],
        method = [RequestMethod.PATCH]
    )
    fun get(
        @PathVariable nickname: String,
        @Valid @RequestBody req: UpdateUserRequest,
        clientInfo: ClientAuthInfo
    ): UserInfoResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@RestController
internal class UpdateUserControllerImpl(
    private val svc: UpdateUserService
) : UpdateUserController {
    override fun get(nickname: String, req: UpdateUserRequest, clientInfo: ClientAuthInfo): UserInfoResponse {
        LOG.debug("{}", clientInfo.requestUri)

        val result = svc.update(nickname, req, clientInfo)

        return UserInfoResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetUserController::class.java)
    }
}

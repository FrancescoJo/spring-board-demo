/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.response.UserInfoResponse
import com.github.fj.board.service.user.GetUserService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface GetUserController {
    @RequestMapping(
        path = [ApiPaths.USER_NICKNAME],
        method = [RequestMethod.GET]
    )
    fun get(@PathVariable nickname: String, clientInfo: ClientAuthInfo): UserInfoResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
@RestController
internal class GetUserControllerImpl(
    private val svc: GetUserService
) : GetUserController {
    override fun get(nickname: String, clientInfo: ClientAuthInfo): UserInfoResponse {
        LOG.debug("{}", clientInfo.requestLine)

        val result = svc.get(nickname, clientInfo)

        return UserInfoResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetUserController::class.java)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.OkResponseDto
import com.github.fj.board.service.auth.DeleteAccountService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface WithdrawController {
    @RequestMapping(
        path = [ApiPaths.ACCOUNT],
        method = [RequestMethod.DELETE]
    )
    fun delete(clientInfo: ClientAuthInfo): OkResponseDto<Boolean>
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@RestController
internal class WithdrawControllerImpl(
    private val svc: DeleteAccountService
) : WithdrawController {
    override fun delete(clientInfo: ClientAuthInfo): OkResponseDto<Boolean> {
        LOG.debug("{}: {}", clientInfo.requestLine)

        val result = svc.delete(clientInfo)

        return OkResponseDto(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(WithdrawController::class.java)
    }
}

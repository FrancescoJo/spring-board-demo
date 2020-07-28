/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.request.CreateBoardRequest
import com.github.fj.board.endpoint.v1.board.response.BoardInfoResponse
import com.github.fj.board.service.board.CreateBoardService
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
 * @since 20 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface CreateBoardController {
    @RequestMapping(
        path = [ApiPaths.BOARD],
        method = [RequestMethod.POST]
    )
    fun create(@Valid @RequestBody req: CreateBoardRequest, clientInfo: ClientAuthInfo): BoardInfoResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@RestController
class CreateBoardControllerImpl(
    private val src: CreateBoardService
) : CreateBoardController {
    override fun create(req: CreateBoardRequest, clientInfo: ClientAuthInfo): BoardInfoResponse {
        LOG.debug("{}: {}", clientInfo.requestLine, req)

        val result = src.create(req, clientInfo)

        return BoardInfoResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CreateBoardController::class.java)
    }
}

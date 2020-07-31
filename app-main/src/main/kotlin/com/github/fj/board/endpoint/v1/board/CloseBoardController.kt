/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.OkResponseDto
import com.github.fj.board.service.board.CloseBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.text.REGEX_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface CloseBoardController {
    @RequestMapping(
        path = [ApiPaths.BOARD_ID],
        method = [RequestMethod.DELETE]
    )
    fun close(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`boardId` must be in a UUID format."
        )
        @PathVariable
        boardId: String,
        clientInfo: ClientAuthInfo
    ): OkResponseDto<Boolean>
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@RestController
internal class CloseBoardControllerImpl(
    private val svc: CloseBoardService
) : CloseBoardController {
    override fun close(boardId: String, clientInfo: ClientAuthInfo): OkResponseDto<Boolean> {
        LOG.debug("{}", clientInfo.requestLine)

        val result = svc.close(UUID.fromString(boardId), clientInfo)

        return OkResponseDto(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CloseBoardController::class.java)
    }
}

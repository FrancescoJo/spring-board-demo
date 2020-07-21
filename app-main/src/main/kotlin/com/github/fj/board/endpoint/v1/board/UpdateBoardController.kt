/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardInfoResponse
import com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequest
import com.github.fj.board.service.board.UpdateBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.util.REGEXP_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid
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
interface UpdateBoardController {
    @RequestMapping(
        path = [ApiPaths.BOARD_ACCESS_ID],
        method = [RequestMethod.PATCH]
    )
    fun update(
        @Pattern(
            regexp = REGEXP_UUID,
            message = "`accessId` must be in a UUID format."
        )
        @PathVariable
        accessId: String,
        @Valid @RequestBody req: UpdateBoardRequest,
        clientInfo: ClientAuthInfo
    ): BoardInfoResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@RestController
class UpdateBoardControllerImpl(
    private val src: UpdateBoardService
) : UpdateBoardController {
    override fun update(accessId: String, req: UpdateBoardRequest, clientInfo: ClientAuthInfo): BoardInfoResponse {
        LOG.debug("{}: {}", clientInfo.requestLine, req)

        val result = src.update(UUID.fromString(accessId), req, clientInfo)

        return BoardInfoResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CreateBoardController::class.java)
    }
}

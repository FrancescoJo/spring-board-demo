/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardInfoListResponse
import com.github.fj.board.endpoint.v1.board.dto.BoardInfoResponse
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortBy
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortOrderBy
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.util.REGEXP_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface GetBoardController {
    @RequestMapping(
        path = [ApiPaths.BOARD_ACCESS_ID],
        method = [RequestMethod.GET]
    )
    fun getOne(
        @Pattern(
            regexp = REGEXP_UUID,
            message = "`accessId` must be in a UUID format."
        )
        @PathVariable
        accessId: String,
        httpReq: HttpServletRequest
    ): BoardInfoResponse

    // Default: group sort by status
    @RequestMapping(
        path = [ApiPaths.BOARDS],
        method = [RequestMethod.GET]
    )
    fun getList(
        @RequestParam(required = false) sortBy: BoardsSortBy = BoardsSortBy.KEY,
        @RequestParam(required = false) orderBy: BoardsSortOrderBy = BoardsSortOrderBy.DESCENDING,
        httpReq: HttpServletRequest
    ): BoardInfoListResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@RestController
internal class GetBoardControllerImpl(
    private val svc: GetBoardService,
    private val authDetector: ControllerClientAuthInfoDetector
) : GetBoardController {
    override fun getOne(accessId: String, httpReq: HttpServletRequest): BoardInfoResponse {
        val clientInfo = authDetector.detectClientAuthInfo().also {
            if (it == null) {
                LOG.debug("{} {}", httpReq.method, httpReq.requestURI)
            } else {
                LOG.debug("{}", it.requestLine)
            }
        }

        val result = svc.getOne(UUID.fromString(accessId), clientInfo)

        return BoardInfoResponse.from(result)
    }

    override fun getList(
        sortBy: BoardsSortBy,
        orderBy: BoardsSortOrderBy,
        httpReq: HttpServletRequest
    ): BoardInfoListResponse {
        val clientInfo = authDetector.detectClientAuthInfo().also {
            if (it == null) {
                LOG.debug("{} {}", httpReq.method, httpReq.requestURI)
            } else {
                LOG.debug("{}", it.requestLine)
            }
        }

        val result = svc.getList(sortBy, orderBy, clientInfo)

        return BoardInfoListResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetBoardController::class.java)
    }
}

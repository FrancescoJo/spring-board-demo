/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.GetBoardController.Companion.GET_LIST_PARAM_ORDER_BY
import com.github.fj.board.endpoint.v1.board.GetBoardController.Companion.GET_LIST_PARAM_SORT_BY
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortBy
import com.github.fj.board.endpoint.v1.board.dto.BoardsSortOrderBy
import com.github.fj.board.endpoint.v1.board.response.BoardInfoListResponse
import com.github.fj.board.endpoint.v1.board.response.BoardInfoResponse
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.util.REGEXP_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
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
        path = [ApiPaths.BOARD_ID],
        method = [RequestMethod.GET]
    )
    fun getOne(
        @Pattern(
            regexp = REGEXP_UUID,
            message = "`boardId` must be in a UUID format."
        )
        @PathVariable
        boardId: String,
        httpReq: HttpServletRequest
    ): BoardInfoResponse

    @RequestMapping(
        path = [ApiPaths.BOARDS],
        method = [RequestMethod.GET]
    )
    fun getList(
        @RequestParam params: MultiValueMap<String, String>,
        httpReq: HttpServletRequest
    ): BoardInfoListResponse

    companion object {
        const val GET_LIST_PARAM_SORT_BY = "sortBy"
        const val GET_LIST_PARAM_ORDER_BY = "orderBy"
    }
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
    override fun getOne(boardId: String, httpReq: HttpServletRequest): BoardInfoResponse {
        val clientInfo = from(httpReq)

        val result = svc.getOne(UUID.fromString(boardId), clientInfo)

        return BoardInfoResponse.from(result)
    }

    override fun getList(
        params: MultiValueMap<String, String>,
        httpReq: HttpServletRequest
    ): BoardInfoListResponse {
        val clientInfo = from(httpReq)

        val sortBy = params.getFirst(GET_LIST_PARAM_SORT_BY)?.let {
            BoardsSortBy.fromString(it)
        } ?: BoardsSortBy.KEY
        val orderBy = params.getFirst(GET_LIST_PARAM_ORDER_BY)?.let {
            BoardsSortOrderBy.fromString(it)
        } ?: BoardsSortOrderBy.ASCENDING

        val result = svc.getList(sortBy, orderBy, clientInfo)

        return BoardInfoListResponse.from(result)
    }

    private fun from(httpReq: HttpServletRequest): ClientAuthInfo? = authDetector.detectClientAuthInfo().also {
        if (it == null) {
            val queryStr = httpReq.queryString?.let { q -> "?$q" } ?: ""

            LOG.debug("{} {}{}", httpReq.method, httpReq.requestURI, queryStr)
        } else {
            LOG.debug("{}", it.requestLine)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetBoardController::class.java)
    }
}

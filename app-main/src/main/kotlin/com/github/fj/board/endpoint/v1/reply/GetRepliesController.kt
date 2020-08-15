/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.common.response.PageableResponse
import com.github.fj.board.endpoint.v1.OptionalClientAuthInfoMixin
import com.github.fj.board.endpoint.v1.reply.GetRepliesController.Companion.GET_LIST_PARAM_COUNT
import com.github.fj.board.endpoint.v1.reply.GetRepliesController.Companion.GET_LIST_PARAM_ORDER_BY
import com.github.fj.board.endpoint.v1.reply.GetRepliesController.Companion.GET_LIST_PARAM_PAGE
import com.github.fj.board.endpoint.v1.reply.GetRepliesController.Companion.GET_LIST_PARAM_SORT_BY
import com.github.fj.board.vo.reply.RepliesFetchCriteria
import com.github.fj.board.vo.SortDirection
import com.github.fj.board.vo.reply.RepliesSortBy
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.lib.text.REGEX_UUID
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
import kotlin.math.min

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface GetRepliesController {
    @RequestMapping(
        path = [ApiPaths.POST_ID_REPLIES],
        method = [RequestMethod.GET]
    )
    fun getLatest(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`postId` must be in a UUID format."
        )
        @PathVariable
        @Suppress("MVCPathVariableInspection") postId: String,

        @RequestParam params: MultiValueMap<String, String>,

        httpReq: HttpServletRequest
    ): PageableResponse<ReplyInfoResponse>

    companion object {
        const val GET_LIST_PARAM_SORT_BY = "sortBy"
        const val GET_LIST_PARAM_ORDER_BY = "orderBy"
        const val GET_LIST_PARAM_PAGE = "page"
        const val GET_LIST_PARAM_COUNT = "count"
    }
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
@RestController
internal class GetRepliesControllerImpl(
    override val authDetector: ControllerClientAuthInfoDetector,
    private val svc: GetRepliesService
) : GetRepliesController, OptionalClientAuthInfoMixin {
    override fun getLatest(
        postId: String,
        params: MultiValueMap<String, String>,
        httpReq: HttpServletRequest
    ): PageableResponse<ReplyInfoResponse> {
        val clientInfo = httpReq.maybeClientAuthInfo(LOG)
        val fetchCriteria = params.toFetchCriteria()
        val result = svc.getListOf(UUID.fromString(postId), clientInfo, fetchCriteria)

        return result.run {
            PageableResponse.create(
                offset = offset,
                totalCount = totalCount,
                data = data.map { ReplyInfoResponse.from(it) }
            )
        }
    }

    private fun MultiValueMap<String, String>.toFetchCriteria() : RepliesFetchCriteria {
        val sortBy = getFirst(GET_LIST_PARAM_SORT_BY)?.let {
            RepliesSortBy.fromString(it)
        } ?: RepliesSortBy.NUMBER
        val sortDirection = (getFirst(GET_LIST_PARAM_ORDER_BY)?.let {
            SortDirection.fromString(it)
        } ?: SortDirection.ASCENDING).direction
        val page = getFirst(GET_LIST_PARAM_PAGE)?.toIntOrNull() ?: GetRepliesService.PAGE_LATEST
        val count = getFirst(GET_LIST_PARAM_COUNT)?.toIntOrNull()?.takeIf {
            it >= GetRepliesService.DEFAULT_REPLY_FETCH_SIZE
        } ?: GetRepliesService.DEFAULT_REPLY_FETCH_SIZE
        val fetchSize = min(count, GetRepliesService.MAXIMUM_REPLY_FETCH_SIZE)

        return RepliesFetchCriteria(
            sortBy = sortBy,
            sortDirection = sortDirection,
            page = page,
            fetchSize = fetchSize
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetRepliesController::class.java)
    }
}

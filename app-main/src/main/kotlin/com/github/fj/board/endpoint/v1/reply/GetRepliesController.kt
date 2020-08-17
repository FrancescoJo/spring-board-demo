/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin
import com.github.fj.board.endpoint.common.response.PageableResponse
import com.github.fj.board.endpoint.v1.OptionalClientAuthInfoMixin
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.service.reply.GetRepliesService.Companion.DEFAULT_REPLY_FETCH_SIZE
import com.github.fj.board.service.reply.GetRepliesService.Companion.MAXIMUM_REPLY_FETCH_SIZE
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.SortDirection
import com.github.fj.board.vo.reply.RepliesSortBy
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
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
@RestController
internal class GetRepliesControllerImpl(
    override val authDetector: ControllerClientAuthInfoDetector,
    private val svc: GetRepliesService
) : GetRepliesController, OptionalClientAuthInfoMixin, FetchCriteriaRequestMixin {
    override fun getLatest(
        postId: String,
        params: MultiValueMap<String, String>,
        httpReq: HttpServletRequest
    ): PageableResponse<ReplyInfoResponse> {
        val clientInfo = httpReq.maybeClientAuthInfo(LOG)
        val fetchCriteria = extractFetchCriteria(
            requestParams = params,
            sortByProvider = { RepliesSortBy.fromString(it) ?: RepliesSortBy.NUMBER },
            defaultSortDirection = SortDirection.DESCENDING,
            defaultPage = ContentsFetchCriteria.PAGE_DEFAULT,
            defaultFetchSizeRange = DEFAULT_REPLY_FETCH_SIZE..MAXIMUM_REPLY_FETCH_SIZE
        )
        val result = svc.getListOf(UUID.fromString(postId), clientInfo, fetchCriteria)

        return result.run {
            PageableResponse.create(
                page = page,
                size = size,
                totalCount = totalCount,
                data = data.map { ReplyInfoResponse.from(it) }
            )
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetRepliesController::class.java)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin
import com.github.fj.board.endpoint.common.response.PageableResponse
import com.github.fj.board.endpoint.v1.OptionalClientAuthInfoMixin
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
import com.github.fj.board.endpoint.v1.post.response.PostInfoDetailedResponse
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.service.post.GetPostService.Companion.DEFAULT_POST_FETCH_SIZE
import com.github.fj.board.service.post.GetPostService.Companion.MAXIMUM_POST_FETCH_SIZE
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.SortDirection
import com.github.fj.board.vo.post.PostsSortBy
import com.github.fj.lib.text.REGEX_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
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
 * @since 12 - Aug - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface GetPostController {
    @RequestMapping(
        path = [ApiPaths.POST_ID],
        method = [RequestMethod.GET]
    )
    fun getOne(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`postId` must be in a UUID format."
        )
        @PathVariable
        postId: String,

        httpReq: HttpServletRequest
    ): PostInfoDetailedResponse

    @RequestMapping(
        path = [ApiPaths.BOARD_ID_POSTS],
        method = [RequestMethod.GET]
    )
    fun getList(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`boardId` must be in a UUID format."
        )
        @PathVariable
        @Suppress("MVCPathVariableInspection") boardId: String,

        @RequestParam params: MultiValueMap<String, String>,

        httpReq: HttpServletRequest
    ): PageableResponse<PostInfoBriefResponse>
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Aug - 2020
 */
@RestController
internal class GetPostControllerImpl(
    override val authDetector: ControllerClientAuthInfoDetector,
    private val svc: GetPostService,
    private val replySvc: GetRepliesService
) : GetPostController, OptionalClientAuthInfoMixin, FetchCriteriaRequestMixin {
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun getOne(postId: String, httpReq: HttpServletRequest): PostInfoDetailedResponse {
        val clientInfo = httpReq.maybeClientAuthInfo(LOG)

        val postAccessId = UUID.fromString(postId)
        val postDetail = svc.getOne(postAccessId, clientInfo)
        val latestReplies = replySvc.getLatestListOf(postAccessId, clientInfo)

        val replyResponse = latestReplies.run {
            PageableResponse.create(
                page = page,
                size = size,
                totalCount = totalCount,
                data = data.map { ReplyInfoResponse.from(it) }
            )
        }

        return PostInfoDetailedResponse.from(postDetail, replyResponse)
    }

    override fun getList(
        boardId: String,
        params: MultiValueMap<String, String>,
        httpReq: HttpServletRequest
    ): PageableResponse<PostInfoBriefResponse> {
        val clientInfo = httpReq.maybeClientAuthInfo(LOG)
        val fetchCriteria = extractFetchCriteria(
            requestParams = params,
            sortByProvider = { PostsSortBy.fromString(it) ?: PostsSortBy.NUMBER },
            defaultSortDirection = SortDirection.DESCENDING,
            defaultPage = ContentsFetchCriteria.PAGE_DEFAULT,
            defaultFetchSizeRange = DEFAULT_POST_FETCH_SIZE..MAXIMUM_POST_FETCH_SIZE
        )

        val result = svc.getListIn(UUID.fromString(boardId), clientInfo, fetchCriteria)

        return result.run {
            PageableResponse.create(
                page = page,
                size = size,
                totalCount = totalCount,
                data = data.map { PostInfoBriefResponse.from(it) }
            )
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetPostController::class.java)
    }
}

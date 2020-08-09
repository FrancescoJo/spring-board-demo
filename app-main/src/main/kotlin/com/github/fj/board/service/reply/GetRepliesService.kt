/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.endpoint.v1.reply.dto.RepliesFetchCriteria
import com.github.fj.board.endpoint.v1.reply.dto.RepliesOrderBy
import com.github.fj.board.endpoint.v1.reply.dto.RepliesSortBy
import com.github.fj.board.vo.Pageable
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface GetRepliesService : ReplyAccessMixin {
    fun getLatestListOf(postId: UUID, clientInfo: ClientAuthInfo?): Pageable<ReplyInfo> =
        getListOf(postId, clientInfo, RepliesFetchCriteria.DEFAULT_LATEST)

    fun getListOf(
        postId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: RepliesFetchCriteria
    ): Pageable<ReplyInfo>

    companion object {
        const val PAGE_LATEST = Integer.MIN_VALUE

        const val DEFAULT_REPLY_FETCH_SIZE = 20
        const val MAXIMUM_REPLY_FETCH_SIZE = 50
    }
}

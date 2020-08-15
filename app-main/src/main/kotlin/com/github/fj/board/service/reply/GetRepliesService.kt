/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.vo.reply.RepliesFetchCriteria
import com.github.fj.board.vo.PagedData
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface GetRepliesService : ReplyAccessMixin {
    fun getLatestListOf(postId: UUID, clientInfo: ClientAuthInfo?): PagedData<ReplyInfo> =
        getListOf(postId, clientInfo, RepliesFetchCriteria.DEFAULT_LATEST)

    fun getListOf(
        postId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: RepliesFetchCriteria
    ): PagedData<ReplyInfo>

    companion object {
        const val PAGE_LATEST = Integer.MIN_VALUE

        const val DEFAULT_REPLY_FETCH_SIZE = 20
        const val MAXIMUM_REPLY_FETCH_SIZE = 50
    }
}

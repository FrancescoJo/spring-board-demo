/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.PagedData
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.RepliesSortBy
import com.github.fj.board.vo.reply.ReplyInfo
import org.springframework.data.domain.Sort
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface GetRepliesService : ReplyAccessMixin {
    fun getLatestListOf(postId: UUID, clientInfo: ClientAuthInfo?): PagedData<ReplyInfo> =
        getListOf(postId, clientInfo, DEFAULT_LATEST)

    fun getListOf(
        postId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: ContentsFetchCriteria<RepliesSortBy>
    ): PagedData<ReplyInfo>

    companion object {
        const val DEFAULT_REPLY_FETCH_SIZE = 20
        const val MAXIMUM_REPLY_FETCH_SIZE = 50

        private val DEFAULT_LATEST = ContentsFetchCriteria.create(
            sortBy = RepliesSortBy.NUMBER,
            sortDirection = Sort.Direction.DESC,
            page = ContentsFetchCriteria.PAGE_DEFAULT,
            fetchSize = DEFAULT_REPLY_FETCH_SIZE
        )
    }
}

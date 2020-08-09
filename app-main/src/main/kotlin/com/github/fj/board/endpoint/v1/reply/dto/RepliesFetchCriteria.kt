/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply.dto

import com.github.fj.board.service.reply.GetRepliesService

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
data class RepliesFetchCriteria(
    val sortBy: RepliesSortBy,
    val orderBy: RepliesOrderBy,
    val page: Int,
    val fetchSize: Int
) {
    companion object {
        val DEFAULT_LATEST = RepliesFetchCriteria(
            sortBy = RepliesSortBy.NUMBER,
            orderBy = RepliesOrderBy.ASCENDING,
            page = GetRepliesService.PAGE_LATEST,
            fetchSize = GetRepliesService.DEFAULT_REPLY_FETCH_SIZE
        )
    }
}

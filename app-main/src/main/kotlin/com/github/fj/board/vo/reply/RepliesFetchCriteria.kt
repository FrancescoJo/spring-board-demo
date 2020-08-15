/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.reply

import com.github.fj.board.service.reply.GetRepliesService
import org.springframework.data.domain.Sort

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
data class RepliesFetchCriteria(
    val sortBy: RepliesSortBy,
    val sortDirection: Sort.Direction,
    val page: Int,
    val fetchSize: Int
) {
    companion object {
        val DEFAULT_LATEST = RepliesFetchCriteria(
            sortBy = RepliesSortBy.NUMBER,
            sortDirection = Sort.Direction.ASC,
            page = GetRepliesService.PAGE_LATEST,
            fetchSize = GetRepliesService.DEFAULT_REPLY_FETCH_SIZE
        )
    }
}

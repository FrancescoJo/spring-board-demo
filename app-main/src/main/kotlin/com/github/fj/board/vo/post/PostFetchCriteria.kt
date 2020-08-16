/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.post

import com.github.fj.board.service.reply.GetRepliesService
import org.springframework.data.domain.Sort

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Aug - 2020
 */
data class PostFetchCriteria(
    val sortBy: PostsSortBy,
    val sortDirection: Sort.Direction,
    val page: Int,
    val fetchSize: Int
) {
    companion object {
        val DEFAULT_LATEST = PostFetchCriteria(
            sortBy = PostsSortBy.NUMBER,
            sortDirection = Sort.Direction.ASC,
            page = GetRepliesService.PAGE_LATEST,
            fetchSize = GetRepliesService.DEFAULT_REPLY_FETCH_SIZE
        )
    }
}

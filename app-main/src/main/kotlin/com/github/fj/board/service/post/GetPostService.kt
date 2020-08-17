/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.PagedData
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import com.github.fj.board.vo.post.PostsSortBy
import org.springframework.data.domain.Sort
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Aug - 2020
 */
interface GetPostService : UserServiceMixin, BoardAuthorisationMixin, PostAccessMixin, PostServiceMixin {
    fun getOne(postId: UUID, clientInfo: ClientAuthInfo?): PostDetailedInfo

    fun getLatestListIn(boardId: UUID, clientInfo: ClientAuthInfo?): PagedData<PostBriefInfo> =
        getListIn(boardId, clientInfo, DEFAULT_LATEST)

    fun getListIn(
        boardId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: ContentsFetchCriteria<PostsSortBy>
    ): PagedData<PostBriefInfo>

    companion object {
        const val DEFAULT_POST_FETCH_SIZE = 20
        const val MAXIMUM_POST_FETCH_SIZE = 30

        private val DEFAULT_LATEST = ContentsFetchCriteria.create(
            sortBy = PostsSortBy.NUMBER,
            sortDirection = Sort.Direction.DESC,
            page = ContentsFetchCriteria.PAGE_DEFAULT,
            fetchSize = DEFAULT_POST_FETCH_SIZE
        )
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.endpoint.v1.post.request.CreatePostRequest
import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
interface CreatePostService : UserServiceMixin, BoardAuthorisationMixin, PostServiceMixin {
    fun create(boardId: UUID, req: CreatePostRequest, clientInfo: ClientAuthInfo): PostBriefInfo
}

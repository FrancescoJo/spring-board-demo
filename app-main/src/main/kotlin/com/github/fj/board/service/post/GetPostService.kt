/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Aug - 2020
 */
interface GetPostService : UserServiceMixin, BoardAuthorisationMixin, PostAccessMixin, PostServiceMixin {
    fun getOne(postId: UUID, clientInfo: ClientAuthInfo?): PostDetailedInfo
}

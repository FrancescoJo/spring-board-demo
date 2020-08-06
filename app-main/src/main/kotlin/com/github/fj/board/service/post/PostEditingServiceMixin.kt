/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.exception.GeneralHttpException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface PostEditingServiceMixin : UserServiceMixin, BoardAuthorisationMixin, PostAccessMixin, PostServiceMixin {
    fun checkEditable(
        postId: UUID,
        clientInfo: ClientAuthInfo,
        onForbiddenException: () -> GeneralHttpException
    ): Pair<User, Post> {
        val self = clientInfo.getCurrentUser()
        val post = postId.getPost()

        with(post) {
            checkIsOwnedBy(self, onForbiddenException)
            board.checkIsWritableFor(self, onForbiddenException)
        }

        return self to post
    }
}

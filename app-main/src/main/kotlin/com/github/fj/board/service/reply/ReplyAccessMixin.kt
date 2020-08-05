/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.exception.GeneralHttpException
import com.github.fj.board.exception.client.reply.CannotCreateReplyException
import com.github.fj.board.exception.client.reply.CannotDeleteReplyException
import com.github.fj.board.exception.client.reply.CannotEditReplyException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.persistence.repository.post.PostRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Aug - 2020
 */
interface ReplyAccessMixin {
    val postRepo: PostRepository

    fun Post.checkIsReplyWritable() = checkOrThrow(
        mode == PostMode.FREE_REPLY || mode == PostMode.REPLY_ONCE,
        CannotCreateReplyException()
    )

    fun Post.checkIsReplyEditable() = checkOrThrow(
        mode == PostMode.FREE_REPLY,
        CannotEditReplyException()
    )

    fun Post.checkIsReplyDeletable() = checkOrThrow(
        mode == PostMode.FREE_REPLY || mode == PostMode.REPLY_NOT_ALLOWED,
        CannotDeleteReplyException()
    )

    fun Post.getRepliesCount(): Long = postRepo.getRepliesCountById(this)

    private fun checkOrThrow(predicate: Boolean, exception: GeneralHttpException) {
        if (!predicate) {
            throw exception
        }
    }
}

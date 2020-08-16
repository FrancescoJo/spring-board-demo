/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.service.board.BoardAccessMixin
import com.github.fj.board.vo.post.PostsSortBy
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
interface PostAccessMixin : BoardAccessMixin {
    val postRepo: PostRepository

    /**
     * @return [Post] of given [UUID] if found. `null` otherwise.
     */
    fun UUID.findPost(): Post? = postRepo.findByAccessId(this)

    /**
     * @return [Post] of given [UUID] if found.
     * @throws PostNotFoundException if there is no post with given [UUID].
     */
    @Throws(PostNotFoundException::class)
    fun UUID.getPost() = findPost() ?: throw PostNotFoundException()

    fun PostsSortBy.toPropertyName(): String = when (this) {
        PostsSortBy.NUMBER        -> "number"
        PostsSortBy.VIEWED_COUNT  -> "viewedCount"
    }
}

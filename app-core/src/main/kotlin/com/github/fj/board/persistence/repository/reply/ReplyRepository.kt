/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.reply

import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
interface ReplyRepository : JpaRepository<Reply, Long>, ReplyRepositoryExtension {
    @Query(
        """
        SELECT r 
        FROM Reply r
        WHERE r.accessId = ?1
          AND r.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
    """
    )
    fun findByAccessId(accessId: UUID): Reply?

    @Query(
        """
            SELECT COUNT(r.id)
            FROM Reply r
            WHERE r.post = ?1
              AND r.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
        """
    )
    fun getCountOf(post: Post): Long

    companion object {
        const val DEFAULT_REPLY_FETCH_SIZE = 20
        const val MAXIMUM_REPLY_FETCH_SIZE = 50
    }
}
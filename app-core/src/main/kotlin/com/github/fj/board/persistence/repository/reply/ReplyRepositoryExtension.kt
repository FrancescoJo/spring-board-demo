/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.reply

import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
interface ReplyRepositoryExtension {
    fun findLatestByPost(post: Post): List<Reply>
}

@Repository
internal class ReplyRepositoryExtensionImpl : ReplyRepositoryExtension {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findLatestByPost(post: Post): List<Reply> = em.createQuery(
        """
            SELECT r 
            FROM Reply r
            WHERE r.post = post
              AND r.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
            ORDER BY r.number DESC
        """.trimIndent(),
        Reply::class.java
    ).setParameter("post", post)
        .setMaxResults(ReplyRepository.DEFAULT_REPLY_FETCH_SIZE)
        .resultList
}

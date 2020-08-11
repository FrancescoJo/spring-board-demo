/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.reply

import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
interface ReplyRepositoryExtension {
    fun findLatestByPost(post: Post, fetchSize: Int): List<Reply>

    fun findByPost(post: Post, options: Pageable): List<Reply>
}

@Repository
internal class ReplyRepositoryExtensionImpl : ReplyRepositoryExtension {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findLatestByPost(post: Post, fetchSize: Int): List<Reply> {
        val options = PageRequest.of(0, fetchSize, Sort.Direction.DESC, "number")

        return findByPost(post, options)
    }

    override fun findByPost(post: Post, options: Pageable): List<Reply> {
        val iteration = options.sort.iterator()
        val orderByClause = if (iteration.hasNext()) {
            "ORDER BY " + iteration.asSequence().joinToString {
                "r.${it.property} ${it.direction}"
            }
        } else {
            ""
        }

        return em.createQuery(
            """
            SELECT r 
            FROM Reply r
            WHERE r.post = :post
              AND r.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
            $orderByClause
        """.trimIndent(),
            Reply::class.java
        ).setParameter("post", post)
            .setFirstResult(options.pageNumber * options.pageSize)
            .setMaxResults(options.pageSize)
            .resultList
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.reply

import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.repository.PageableQuery
import com.github.fj.board.persistence.repository.PageableQueryHelperMixin
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
interface ReplyRepositoryExtension {
    fun findByPost(post: Post, options: PageableQuery): List<Reply>

    // Map<postId: Long, count: Long>
    fun getCountsOf(posts: List<Post>): Map<Long, Long>
}

@Repository
internal class ReplyRepositoryExtensionImpl : ReplyRepositoryExtension, PageableQueryHelperMixin {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findByPost(post: Post, options: PageableQuery): List<Reply> = em.createQuery(
        """
            SELECT r 
            FROM Reply r
            WHERE r.post = :post
              AND r.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
            ${options.toOrderByClause("r")}
        """.trimIndent(), Reply::class.java
    ).setParameter("post", post)
        .setFirstResult(options.offset)
        .setMaxResults(options.fetchSize)
        .resultList

    override fun getCountsOf(posts: List<Post>): Map<Long, Long> {
        val dtoClassName =
            "com.github.fj.board.persistence.repository.reply.ReplyRepositoryExtensionImpl\$GetCountsOfData"

        val countList = em.createQuery(
            """
            SELECT new $dtoClassName(r.post.id, COUNT(r.id))
            FROM Reply r
            WHERE r.post IN :posts
              AND r.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
            GROUP BY r.post
        """.trimIndent(), GetCountsOfData::class.java
        ).setParameter("posts", posts)
            .resultList

        return countList.associateBy(keySelector = { it.postId }, valueTransform = { it.count })
    }

    internal data class GetCountsOfData(val postId: Long, val count: Long)
}

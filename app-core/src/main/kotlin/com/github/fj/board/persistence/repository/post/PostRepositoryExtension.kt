/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.post

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.repository.PageableQueryHelperMixin
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Aug - 2020
 */
interface PostRepositoryExtension {
    fun findAllByBoard(board: Board, options: Pageable): List<Post>
}

@Repository
internal class PostRepositoryExtensionImpl : PostRepositoryExtension, PageableQueryHelperMixin {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findAllByBoard(board: Board, options: Pageable): List<Post> = em.createQuery(
        """
            SELECT p
            FROM Post p
            WHERE p.board = :board
              AND p.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
            ${options.toOrderByClause("r")}
        """.trimIndent(), Post::class.java
    ).setParameter("board", board)
        .setFirstResult(options.toFirstResultOffset())
        .setMaxResults(options.pageSize)
        .resultList
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.post

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.post.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
interface PostRepository : JpaRepository<Post, Long>, PostRepositoryExtension {
    @Query(
        """
        SELECT p
        FROM Post p
        WHERE p.accessId = ?1
          AND p.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
    """
    )
    fun findByAccessId(accessId: UUID): Post?

    @Query(
        """
            SELECT COUNT(p.id)
            FROM Post p
            WHERE p.board = ?1
              AND p.status <> com.github.fj.board.persistence.model.post.ContentStatus.DELETED
        """
    )
    fun getCountOf(board: Board): Long
}

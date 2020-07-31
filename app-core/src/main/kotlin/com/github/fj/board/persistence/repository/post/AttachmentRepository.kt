/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.post

import com.github.fj.board.persistence.entity.post.Attachment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Jul - 2020
 */
interface AttachmentRepository : JpaRepository<Attachment, Long> {
    @Query(
        """
        SELECT a
        FROM Attachment a
        WHERE a.accessId IN ?1
    """
    )
    fun findAllByAccessIds(accessIds: List<UUID>): List<Attachment>
}

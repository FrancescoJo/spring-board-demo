/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.reply

import com.github.fj.board.persistence.entity.post.Post
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
interface ReplyRepository : JpaRepository<Post, Long>

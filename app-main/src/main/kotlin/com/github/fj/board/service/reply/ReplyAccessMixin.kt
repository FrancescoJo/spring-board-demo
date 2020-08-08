/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.exception.client.reply.ReplyNotFoundException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.service.post.PostAccessMixin
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Aug - 2020
 */
interface ReplyAccessMixin : PostAccessMixin {
    val replyRepo: ReplyRepository

    fun Post.getRepliesCount(): Long = replyRepo.getCountOf(this)

    fun UUID.findReply(): Reply? = replyRepo.findByAccessId(this)

    @Throws(ReplyNotFoundException::class)
    fun UUID.getReply() = findReply() ?: throw ReplyNotFoundException()
}

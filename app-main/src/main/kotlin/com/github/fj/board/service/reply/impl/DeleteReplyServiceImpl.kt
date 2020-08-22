/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply.impl

import com.github.fj.board.exception.client.reply.CannotDeleteReplyException
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.reply.DeleteReplyService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.Future

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Aug - 2020
 */
@Service
internal class DeleteReplyServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository
) : DeleteReplyService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun delete(replyId: UUID, clientInfo: ClientAuthInfo): Boolean {
        val (_, reply) = checkEditable(replyId, clientInfo, onForbiddenException = { CannotDeleteReplyException() })

        reply.apply {
            status = ContentStatus.DELETED

            applyLastActivityWith(clientInfo, utcNow())
        }.also {
            replyRepo.save(it)
        }

        return true
    }

    override fun deleteAllIn(postId: UUID): Future<Boolean> {
        val post = postId.findPost() ?: run {
            LOG.warn("Unable to delete all posts of '{}'", postId)
            return AsyncResult(false)
        }

        replyRepo.deleteAllByPost(post)
        return AsyncResult(true)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DeleteReplyService::class.java)
    }
}

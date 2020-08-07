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
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Aug - 2020
 */
@Service
class DeleteReplyServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository
) : DeleteReplyService {
    @Transactional
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
}

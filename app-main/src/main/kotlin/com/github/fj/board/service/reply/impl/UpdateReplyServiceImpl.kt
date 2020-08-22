/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply.impl

import com.github.fj.board.endpoint.v1.reply.request.UpdateReplyRequest
import com.github.fj.board.exception.client.reply.CannotEditReplyException
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.reply.UpdateReplyService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
@Service
internal class UpdateReplyServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository
) : UpdateReplyService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun update(replyId: UUID, req: UpdateReplyRequest, clientInfo: ClientAuthInfo): ReplyInfo {
        val (_, reply) = checkEditable(replyId, clientInfo, onForbiddenException = { CannotEditReplyException() })

        reply.apply {
            this.contents = req.content
            this.edited = true

            applyLastActivityWith(clientInfo, utcNow())
        }.also {
            replyRepo.save(it)
        }

        return ReplyInfo.from(reply)
    }
}

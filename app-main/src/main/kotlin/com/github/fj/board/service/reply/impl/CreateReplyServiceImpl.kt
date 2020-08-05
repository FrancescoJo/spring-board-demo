/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply.impl

import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest
import com.github.fj.board.exception.client.reply.CannotCreateReplyException
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.reply.CreateReplyService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
@Service
internal class CreateReplyServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    private val replyRepo: ReplyRepository
) : CreateReplyService {
    @Transactional
    override fun create(postId: UUID, req: CreateReplyRequest, clientInfo: ClientAuthInfo): ReplyInfo {
        val self = clientInfo.getCurrentUser()
        val post = postId.getPost()
        post.board.checkIsWritableFor(self, onForbiddenException = { CannotCreateReplyException() })
        post.checkIsReplyWritable()

        val reply = Reply().apply {
            this.accessId = UUID.randomUUID()
            this.status = ContentStatus.NOT_REVIEWED
            this.post = post
            this.creator = self

            this.edited = false
            this.number = post.getRepliesCount() + 1
            this.contents = req.content

            applyLastActivityWith(clientInfo, utcNow())
        }.also {
            replyRepo.save(it)
        }

        return ReplyInfo.from(reply)
    }
}

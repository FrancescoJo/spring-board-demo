/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.exception.client.post.CannotDeletePostException
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.DeletePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
@Service
internal class DeletePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository,
    private val attachmentRepo: AttachmentRepository
) : DeletePostService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun delete(postId: UUID, clientInfo: ClientAuthInfo): Boolean {
        val (_, post) = checkEditable(postId, clientInfo, onForbiddenException = { CannotDeletePostException() })

        post.apply {
            this.status = ContentStatus.DELETED
            attachments.forEach {
                it.apply {
                    this.status = ContentStatus.DELETED
                }
            }

            applyLastActivityWith(clientInfo, utcNow())
        }

        attachmentRepo.saveAll(post.attachments)
        postRepo.save(post)

        return true
    }
}

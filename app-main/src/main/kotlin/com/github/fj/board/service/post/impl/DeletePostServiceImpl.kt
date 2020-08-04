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
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.DeletePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
@Service
internal class DeletePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    private val attachmentRepo: AttachmentRepository
) : DeletePostService {
    @Transactional
    override fun delete(postId: UUID, clientInfo: ClientAuthInfo): Boolean {
        val self = clientInfo.getCurrentUser()
        val post = postId.getPost()
        if (post.creator.id != self.id) {
            throw CannotDeletePostException()
        }
        post.board.also {
            it.checkIsWritableFor(self, onForbiddenException = { CannotDeletePostException() })
        }

        post.apply {
            this.status = ContentStatus.DELETED
        }

        post.attachments.forEach {
            it.apply {
                this.status = ContentStatus.DELETED
            }
        }

        attachmentRepo.saveAll(post.attachments)
        postRepo.save(post)

        return true
    }
}

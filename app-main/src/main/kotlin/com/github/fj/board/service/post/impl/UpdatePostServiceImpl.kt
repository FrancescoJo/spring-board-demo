/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.DeleteAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentMode
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdatePostRequest
import com.github.fj.board.exception.client.post.AttachmentNotFoundException
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.UpdatePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
@Service
internal class UpdatePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    private val attachmentRepo: AttachmentRepository
) : UpdatePostService {
    @Transactional
    override fun update(boardId: UUID, req: UpdatePostRequest, clientInfo: ClientAuthInfo): PostBriefInfo {
        val self = clientInfo.getCurrentUser()
        val board = boardId.getBoard().also {
            it.isWritableOrThrowFor(self)
        }
        val post = UUID.fromString(req.accessId).getPost()

        attachmentRepo.saveAll(updateAttachmentsOf(post, req))

        val updatedPost = post.apply {
            this.mode = req.mode
            this.title = req.title
            this.contents = req.contents
            this.edited = true

            applyLastActivityWith(clientInfo, utcNow())
        }

        return PostBriefInfo.from(postRepo.save(updatedPost))
    }

    private fun updateAttachmentsOf(post: Post, req: UpdatePostRequest): List<Attachment> {
        val attachmentReqGroup = req.attachments?.groupBy { it.mode } ?: emptyMap()

        @Suppress("UNCHECKED_CAST")
        val additions = (attachmentReqGroup.getMode(UpdateAttachmentMode.CREATE) as List<CreateAttachmentRequest>)
            .map { it.toEntityOf(post) }

        @Suppress("UNCHECKED_CAST")
        val deleteTargets = (attachmentReqGroup.getMode(UpdateAttachmentMode.DELETE) as List<DeleteAttachmentRequest>)
            .map { UUID.fromString(it.accessId) }

        val deletions = attachmentRepo.findAllByAccessIds(deleteTargets).also { entities ->
            if (deleteTargets.size != entities.size) {
                throw AttachmentNotFoundException()
            }

            entities.forEach {
                it.status = ContentStatus.DELETED
            }
        }

        return additions + deletions
    }

    /**
     * @return
     * For deletions: `List<DeleteAttachmentRequest>`
     * For additions: `List<CreateAttachmentRequest>`
     */
    @VisibleForTesting
    fun Map<UpdateAttachmentMode, List<UpdateAttachmentRequest>>.getMode(mode: UpdateAttachmentMode): List<Any> {
        val list = (this[mode] ?: emptyList())

        return when (mode) {
            UpdateAttachmentMode.DELETE -> list.map { it.payload as DeleteAttachmentRequest }
            UpdateAttachmentMode.CREATE -> list.map { it.payload as CreateAttachmentRequest }
            else -> list
        }
    }
}

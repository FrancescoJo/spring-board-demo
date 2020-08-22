/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.endpoint.v1.post.request.AttachmentModeRequest
import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.DeleteAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdatePostRequest
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.post.AttachmentNotFoundException
import com.github.fj.board.exception.client.post.CannotEditPostException
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.PostEditingServiceMixin
import com.github.fj.board.service.post.UpdatePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
@Service
internal class UpdatePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository,
    private val attachmentRepo: AttachmentRepository
) : UpdatePostService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun update(postId: UUID, req: UpdatePostRequest, clientInfo: ClientAuthInfo): PostBriefInfo {
        val (_, post) = checkEditable(postId, clientInfo, onForbiddenException = { CannotEditPostException() })

        val newAttachments = updateAttachmentsOf(post, req)

        val attachmentsSize = attachmentRepo.getCountOf(post)
        val newCreatedSize = req.attachments.filter { it.mode == AttachmentModeRequest.CREATE }.size

        if (attachmentsSize + newCreatedSize > PostEditingServiceMixin.MAXIMUM_ATTACHMENTS_PER_POST) {
            throw IllegalRequestException()
        }

        attachmentRepo.saveAll(newAttachments)

        val updatedPost = post.apply {
            this.mode = req.mode
            this.title = req.title
            this.contents = req.contents
            this.edited = true

            applyLastActivityWith(clientInfo, utcNow())
        }

        val replyCount = updatedPost.getRepliesCount()

        return PostBriefInfo.from(postRepo.save(updatedPost), replyCount)
    }

    private fun updateAttachmentsOf(post: Post, req: UpdatePostRequest): List<Attachment> {
        val attachmentReqGroup = req.attachments.groupBy { it.mode }

        @Suppress("UNCHECKED_CAST")
        val additions = (attachmentReqGroup.getMode(AttachmentModeRequest.CREATE) as List<CreateAttachmentRequest>)
            .map { it.toEntityOf(post) }

        @Suppress("UNCHECKED_CAST")
        val deleteTargets = (attachmentReqGroup.getMode(AttachmentModeRequest.DELETE) as List<DeleteAttachmentRequest>)
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
    fun Map<AttachmentModeRequest, List<UpdateAttachmentRequest>>.getMode(mode: AttachmentModeRequest): List<Any> {
        val list = (this[mode] ?: emptyList())

        return when (mode) {
            AttachmentModeRequest.DELETE -> list.map { it.payload as DeleteAttachmentRequest }
            AttachmentModeRequest.CREATE -> list.map { it.payload as CreateAttachmentRequest }
            else -> list
        }
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.endpoint.v1.post.dto.CreatePostRequest
import com.github.fj.board.exception.client.post.CannotCreatePostException
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.CreatePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@Service
internal class CreatePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    private val postRepo: PostRepository,
    private val attachmentRepo: AttachmentRepository
) : CreatePostService {
    @Transactional
    override fun create(boardId: UUID, req: CreatePostRequest, clientInfo: ClientAuthInfo): PostBriefInfo {
        val self = clientInfo.getCurrentUser()
        val board = boardId.getBoard()

        when {
            board.status != BoardStatus.NORMAL -> throw CannotCreatePostException()
            board.mode == BoardMode.READ_ONLY  -> throw CannotCreatePostException()
        }

        val createdPost = Post().apply {
            this.accessId = UUID.randomUUID()
            this.status = PostStatus.NOT_REVIEWED
            this.mode = req.mode
            this.board = board
            this.user = self
            this.parentThread = null
            this.lastModifiedDate = utcNow()
            this.lastModifiedIp = clientInfo.remoteAddr
            this.lastModifiedPlatformType = clientInfo.platformType
            this.edited = false
            this.number = board.getPostsCount() + 1
            this.title = req.title
            this.contents = req.content
            this.viewedCount = 0
        }

        val attachments = req.attachments.map {
            Attachment().apply {
                this.accessId = UUID.randomUUID()
                this.post = createdPost
                this.name = it.name
                this.uri = it.uri
                this.mimeType = it.mimeType
            }
        }

        postRepo.save(createdPost)
        attachmentRepo.saveAll(attachments)

        return PostBriefInfo.from(createdPost)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.endpoint.v1.post.request.CreatePostRequest
import com.github.fj.board.exception.client.post.CannotCreatePostException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.CreatePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@Service
internal class CreatePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val replyRepo: ReplyRepository,
    private val postRepo: PostRepository,
    private val attachmentRepo: AttachmentRepository
) : CreatePostService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun create(boardId: UUID, req: CreatePostRequest, clientInfo: ClientAuthInfo): PostBriefInfo {
        val self = clientInfo.getCurrentAccessibleUser()
        val board = boardId.getBoard().also {
            it.checkIsWritableFor(self, onForbiddenException = { CannotCreatePostException() })
        }

        val createdPost = Post().apply {
            this.accessId = UUID.randomUUID()
            this.status = ContentStatus.NOT_REVIEWED
            this.mode = req.mode
            this.board = board
            this.creator = self
            this.edited = false
            this.number = board.getPostsCount() + 1
            this.title = req.title
            this.contents = req.contents
            this.viewedCount = 0

            applyLastActivityWith(clientInfo, utcNow())
        }.also {
            postRepo.save(it)
        }

        req.attachments.map {
            it.toEntityOf(createdPost)
        }.also {
            attachmentRepo.saveAll(it)
        }

        return PostBriefInfo.from(createdPost, 0L)
    }
}

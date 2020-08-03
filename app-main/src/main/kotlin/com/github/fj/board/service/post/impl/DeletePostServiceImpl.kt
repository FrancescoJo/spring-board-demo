/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.DeletePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.stereotype.Service
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
    private val attachmentRepo: AttachmentRepository
) : DeletePostService {
    override fun delete(boardId: UUID, postId: UUID, clientInfo: ClientAuthInfo): Boolean {
        TODO("Not yet implemented")
    }
}

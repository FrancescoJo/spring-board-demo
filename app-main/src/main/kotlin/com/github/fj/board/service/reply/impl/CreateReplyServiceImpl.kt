/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply.impl

import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.reply.CreateReplyService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import org.springframework.stereotype.Service

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
    override fun create(postId: String, req: CreateReplyRequest, clientInfo: ClientAuthInfo): ReplyInfo {
        TODO("Not yet implemented")
    }
}

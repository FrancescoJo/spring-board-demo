/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Aug - 2020
 */
@Service
internal class GetPostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository
    ) : GetPostService {
    @Transactional
    override fun getOne(postId: UUID, clientInfo: ClientAuthInfo?): PostDetailedInfo {
        val post = postId.getPost().also {
            it.board.checkAccessibleFor(clientInfo)
        }
        ++post.viewedCount
        val replyCount = post.getRepliesCount()

        return PostDetailedInfo.from(post, replyCount, post.attachments).also {
            postRepo.save(post)
        }
    }
}

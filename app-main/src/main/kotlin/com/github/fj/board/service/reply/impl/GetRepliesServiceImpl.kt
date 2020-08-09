/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply.impl

import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.vo.Pageable
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
@Service
class GetRepliesServiceImpl(
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository
) : GetRepliesService {
    @Transactional
    override fun getLatestListOf(postId: UUID, clientInfo: ClientAuthInfo?): Pageable<ReplyInfo> {
        val post = postId.getPost().also {
            it.board.checkAccessibleFor(clientInfo)
        }

        val totalCount = replyRepo.getCountOf(post)
        val data = replyRepo.findLatestByPost(post).map { ReplyInfo.from(it) }
        val offset = totalCount - data.size

        return Pageable.create(offset, totalCount, data)
    }
}

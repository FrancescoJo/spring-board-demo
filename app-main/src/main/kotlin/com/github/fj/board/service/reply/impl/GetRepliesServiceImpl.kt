/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply.impl

import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.PagedData
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.RepliesSortBy
import com.github.fj.board.vo.reply.ReplyInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
@Service
internal class GetRepliesServiceImpl(
    override val boardRepo: BoardRepository,
    override val postRepo: PostRepository,
    override val replyRepo: ReplyRepository
) : GetRepliesService {
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    override fun getListOf(
        postId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: ContentsFetchCriteria<RepliesSortBy>
    ): PagedData<ReplyInfo> {
        val post = postId.getPost().also {
            it.board.checkAccessibleFor(clientInfo)
        }

        val totalCount = replyRepo.getCountOf(post)
        val data = replyRepo.findByPost(post, fetchCriteria.toPageableQuery(totalCount) { it.toPropertyName() })

        return fetchCriteria.run {
            PagedData.create(page, fetchSize, totalCount, data.map { ReplyInfo.from(it) })
        }
    }
}

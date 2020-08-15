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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    override fun getListOf(
        postId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: ContentsFetchCriteria<RepliesSortBy>
    ): PagedData<ReplyInfo> {
        val post = postId.getPost().also {
            it.board.checkAccessibleFor(clientInfo)
        }

        val totalCount = replyRepo.getCountOf(post)
        val data = if (fetchCriteria.page < 0) {
            replyRepo.findLatestByPost(post, fetchCriteria.fetchSize)
        } else {
            replyRepo.findByPost(post, fetchCriteria.toPageable())
        }
        val offset = totalCount - data.size

        return PagedData.create(offset, totalCount, data.map { ReplyInfo.from(it) })
    }

    private fun ContentsFetchCriteria<RepliesSortBy>.toPageable(): Pageable =
        PageRequest.of(page, fetchSize, sortDirection, sortBy.toPropertyName())
}

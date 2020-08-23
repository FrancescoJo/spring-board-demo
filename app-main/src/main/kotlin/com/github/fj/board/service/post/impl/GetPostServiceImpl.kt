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
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.PagedData
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import com.github.fj.board.vo.post.PostsSortBy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun getListIn(
        boardId: UUID,
        clientInfo: ClientAuthInfo?,
        fetchCriteria: ContentsFetchCriteria<PostsSortBy>
    ): PagedData<PostBriefInfo> {
        val board = boardId.getBoard().also {
            it.checkAccessibleFor(clientInfo)
        }

        val totalCount = postRepo.getCountOf(board)
        val data = postRepo.findAllByBoard(board, fetchCriteria.toPageableQuery(totalCount) { it.toPropertyName() })
        val repliesCount = replyRepo.getCountsOf(data)

        return fetchCriteria.run {
            PagedData.create(
                page,
                fetchSize,
                totalCount,
                data.map { PostBriefInfo.from(it, repliesCount[it.id] ?: 0L) })
        }
    }
}

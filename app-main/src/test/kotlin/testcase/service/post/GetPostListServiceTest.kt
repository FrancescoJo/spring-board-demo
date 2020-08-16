/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.service.post.GetPostService.Companion.DEFAULT_POST_FETCH_SIZE
import com.github.fj.board.service.post.impl.GetPostServiceImpl
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.post.PostsSortBy
import com.github.fj.lib.collection.iterationsOf
import com.nhaarman.mockitokotlin2.any
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.data.domain.Sort
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Aug - 2020
 */
class GetPostListServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: GetPostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = GetPostServiceImpl(userRepo, boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if target board is not found`() {
        // given:
        val accessId = UUID.randomUUID()

        // expect:
        assertThrows<BoardNotFoundException> {
            sut.getLatestListIn(accessId, null)
        }
    }

    @Test
    fun `fail if user is unauthenticated and board is non-public`() {
        // given:
        val (_, _) = prepareSelf()
        val board = BoardBuilder.createRandom().apply {
            this.access = BoardAccess.MEMBERS_ONLY
        }

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(null)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.getLatestListIn(board.accessId, null)
        }
    }

    @Test
    fun `page is recalculated if less than 1 is given`() {
        // given:
        val (_, self) = prepareSelf()
        val board = BoardBuilder.createRandom()
        val postsCount = 10
        val posts = postsCount.iterationsOf { PostBuilder.createRandomOf(board, self) }
        val repliesCount = HashMap<Long, Long>().apply {
            posts.forEach { put(it.id, 10L) }
        }

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)
        `when`(postRepo.findAllByBoard(any(), any())).thenReturn(posts)
        `when`(replyRepo.getCountsOf(posts)).thenReturn(repliesCount)
        `when`(postRepo.getCountOf(board)).thenReturn(posts.size.toLong())

        // then:
        val actual = sut.getLatestListIn(board.accessId, null)

        // expect:
        assertThat(actual.page, `is`(1))
        assertThat(actual.size, `is`(20))
        assertThat(actual.totalCount, `is`(postsCount.toLong()))
        assertTrue(actual.data.all { it.replyCount == 10L } )
    }

    @Test
    fun `list of posts are returned for given criteria`() {
        // given:
        val (_, self) = prepareSelf()
        val board = BoardBuilder.createRandom()
        val page = 4
        val totalCount = 100
        val posts = DEFAULT_POST_FETCH_SIZE.iterationsOf { PostBuilder.createRandomOf(board, self) }
        val repliesCount = HashMap<Long, Long>().apply {
            posts.forEach { put(it.id, 0L) }
        }
        val criteria = ContentsFetchCriteria.create(
            sortBy = PostsSortBy.NUMBER,
            sortDirection = Sort.Direction.DESC,
            page = page,
            fetchSize = DEFAULT_POST_FETCH_SIZE
        )

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)
        `when`(postRepo.findAllByBoard(any(), any())).thenReturn(posts)
        `when`(replyRepo.getCountsOf(posts)).thenReturn(repliesCount)
        `when`(postRepo.getCountOf(board)).thenReturn(totalCount.toLong())

        // then:
        val actual = sut.getListIn(board.accessId, null, criteria)

        // expect:
        assertThat(actual.page, `is`(page))
        assertThat(actual.size, `is`(20))
        assertThat(actual.totalCount, `is`(totalCount.toLong()))
        assertTrue(actual.data.all { it.replyCount == 0L } )
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.service.reply.GetRepliesService.Companion.DEFAULT_REPLY_FETCH_SIZE
import com.github.fj.board.service.reply.impl.GetRepliesServiceImpl
import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.lib.collection.iterationsOf
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import test.com.github.fj.board.persistence.entity.reply.ReplyBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import testcase.service.post.AbstractPostServiceTestTemplate
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class GetLatestRepliesServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: GetRepliesService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = GetRepliesServiceImpl(boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if parent post is not found`() {
        // given:
        val (clientInfo, _) = prepareSelf()

        // expect:
        assertThrows<PostNotFoundException> {
            sut.getLatestListOf(UUID.randomUUID(), clientInfo)
        }
    }

    @Test
    fun `fail if user is unauthenticated and post is in non-public board`() {
        // given:
        val (_, _, post) = postPreconditions()
        post.board.access = BoardAccess.MEMBERS_ONLY

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // expect:
        assertThrows<BoardNotFoundException> {
            sut.getLatestListOf(post.accessId, null)
        }
    }

    @Test
    fun `latest list is returned if there are replies in given post`() {
        // given:
        val numReplies = 50
        val (_, _, post) = postPreconditions()
        val replies = numReplies.iterationsOf {
            ReplyBuilder.createRandomOf(post, UserBuilder.createRandom())
        }

        // when:
        val size = DEFAULT_REPLY_FETCH_SIZE
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)
        `when`(replyRepo.getCountOf(post)).thenReturn(numReplies.toLong())
        `when`(replyRepo.findByPost(eq(post), any())).thenReturn(replies.subList(numReplies - 1 - size, numReplies - 1))

        // then:
        val actual = sut.getLatestListOf(post.accessId, null)

        // expect:
        assertThat(actual.page, `is`(ContentsFetchCriteria.PAGE_DEFAULT))
        assertThat(actual.size, `is`(size))
        assertThat(actual.totalCount.toInt(), `is`(replies.size))
    }
}

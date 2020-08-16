/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint.v1.post

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.v1.post.GetPostController
import com.github.fj.board.endpoint.v1.post.GetPostControllerImpl
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.vo.PagedData
import com.github.fj.board.vo.post.PostDetailedInfo
import com.github.fj.board.vo.reply.ReplyInfo
import com.github.fj.lib.collection.iterationsOf
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import test.com.github.fj.board.persistence.entity.reply.ReplyBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 13 - Aug - 2020
 */
class PostControllerTest {
    @Mock
    private lateinit var authDetector: ControllerClientAuthInfoDetector

    @Mock
    private lateinit var svc: GetPostService

    @Mock
    private lateinit var replySvc: GetRepliesService

    private lateinit var sut: GetPostController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = GetPostControllerImpl(authDetector, svc, replySvc)
    }

    @Test
    fun `browsing single post also fetches latest replies within it`() {
        // given:
        val owner = UserBuilder.createRandom()
        val board = BoardBuilder.createRandom()
        val post = PostBuilder.createRandomOf(board, owner)
        val attachments = emptyList<Attachment>()
        val replyCount = 10
        val replies = replyCount.iterationsOf {
            ReplyBuilder.createRandomOf(post, UserBuilder.createRandom())
        }

        // when:
        `when`(svc.getOne(any(), anyOrNull())).thenReturn(PostDetailedInfo.from(post, replyCount.toLong(), attachments))
        `when`(replySvc.getLatestListOf(any(), anyOrNull())).thenReturn(
            PagedData.create(page = 0, size = replies.size, totalCount = 100, data = replies.map { ReplyInfo.from(it) })
        )

        // then:
        val actual = sut.getOne(post.accessId.toString(), mockLocalhostServletRequest())

        // expect:
        verify(svc, times(1)).getOne(post.accessId, null)
        verify(replySvc, times(1)).getLatestListOf(post.accessId, null)

        // and:
        assertThat(actual.postId, `is`(post.accessId.toString()))
        assertThat(actual.attachments, `is`(emptyList()))
        assertThat(actual.replies.data.size, `is`(replyCount))
    }
}

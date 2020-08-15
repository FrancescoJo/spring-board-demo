/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.service.post.impl.GetPostServiceImpl
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
class GetPostSingleServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: GetPostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = GetPostServiceImpl(userRepo, boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if target post is not found`() {
        // given:
        val (_, _, post) = postPreconditions()

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(null)

        // then:
        assertThrows<PostNotFoundException> {
            sut.getOne(post.accessId, null)
        }
    }

    @Test
    fun `fail if user is unauthenticated and post is in non-public board`() {
        // given:
        val (_, _, post) = postPreconditions()
        post.board.access = BoardAccess.MEMBERS_ONLY

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.getOne(post.accessId, null)
        }
    }

    @Test
    fun `post detail and latest replies are returned`() {
        // given:
        val (_, _, post) = postPreconditions()

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        val actual = sut.getOne(post.accessId, null)

        // expect:
        assertThat(actual.boardId, `is`(post.board.accessId))
        assertThat(actual.accessId, `is`(post.accessId))
        assertTrue(!actual.edited)
        assertThat(actual.writerNickname, `is`(post.creator.nickname))
        assertThat(actual.writerLoginName, `is`(post.creator.authentication.loginName))
        assertThat(actual.title, `is`(post.title))
        assertThat(actual.contents, `is`(post.contents))
        assertThat(actual.attachments.size, `is`(post.attachments.size))
    }

    @Test
    fun `viewCount is increased if different viewers access post`() {
        // given:
        val (_, _, post) = postPreconditions()
        val oldViewCount = post.viewedCount

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        sut.getOne(post.accessId, null)
        val postCaptor: KArgumentCaptor<Post> = argumentCaptor()

        // expect:
        verify(postRepo, times(1)).save(postCaptor.capture())

        val savedPost = postCaptor.firstValue
        assertThat(savedPost.viewedCount, `is`(oldViewCount + 1))
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotDeletePostException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.service.post.DeletePostService
import com.github.fj.board.service.post.impl.DeletePostServiceImpl
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
class DeletePostServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: DeletePostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = DeletePostServiceImpl(userRepo, boardRepo, postRepo, replyRepo, attachmentRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.delete(UUID.randomUUID(), clientInfo)
        }
    }

    @Test
    fun `fail if target post is not found`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()

        // expect:
        assertThrows<PostNotFoundException> {
            sut.delete(post.accessId, clientInfo)
        }
    }

    @Test
    fun `fail if target post is not owned`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val board = post.board
        val otherUserPost = PostBuilder.createRandomOf(board, UserBuilder.createRandom())

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)
        `when`(postRepo.findByAccessId(otherUserPost.accessId)).thenReturn(otherUserPost)

        // then:
        assertThrows<CannotDeletePostException> {
            sut.delete(otherUserPost.accessId, clientInfo)
        }
    }

    @ParameterizedTest(name = "Cannot delete post({1}) for \"{0}\" board")
    @MethodSource("testCannotDeletePost")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {
        // given:
        val (clientInfo, user) = prepareSelf()
        val post = PostBuilder.createRandomOf(board, user)

        // when:
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)

        // then:
        Assertions.assertThrows(expectedException.java) {
            sut.delete(post.accessId, clientInfo)
        }
    }

    @Test
    fun `post is deleted if request is valid`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val board = post.board

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        val result = sut.delete(post.accessId, clientInfo)

        // expect:
        assertTrue(result)

        // when: "Capture deleted post"
        val postCaptor: KArgumentCaptor<Post> = argumentCaptor()
        verify(postRepo, times(1)).save(postCaptor.capture())
        val deletedPost = postCaptor.firstValue

        // then: "Expect changes on deleted post"
        assertThat(deletedPost.accessId, `is`(post.accessId))
        assertThat(deletedPost.status, `is`(ContentStatus.DELETED))

        // when: "Capture deleted attachments"
        val attachmentsCaptor: KArgumentCaptor<List<Attachment>> = argumentCaptor()
        verify(attachmentRepo, times(1)).saveAll(attachmentsCaptor.capture())
        val deletedAttachment = attachmentsCaptor.firstValue

        // then:
        assertThat(deletedAttachment.size, `is`(post.attachments.size))
        assertTrue(deletedAttachment.all { it.status == ContentStatus.DELETED })
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotDeletePost(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardNotFoundException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", CannotDeletePostException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                ),
                Arguments.of(
                    "Mode:WRITE_ONCE", CannotDeletePostException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.WRITE_ONCE)
                        .build()
                )
            )
        }
    }
}

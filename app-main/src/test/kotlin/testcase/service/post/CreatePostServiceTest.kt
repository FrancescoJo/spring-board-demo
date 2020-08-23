/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotCreatePostException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.post.CreatePostService
import com.github.fj.board.service.post.impl.CreatePostServiceImpl
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.`when`
import test.com.github.fj.board.endpoint.v1.post.dto.CreatePostRequestBuilder
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class CreatePostServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: CreatePostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = CreatePostServiceImpl(userRepo, boardRepo, replyRepo, postRepo, attachmentRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreatePostRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.create(UUID.randomUUID(), req, clientInfo)
        }
    }

    @Test
    fun `fail if board for given boardId is not present`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val req = CreatePostRequestBuilder.createRandom()
        val board = post.board

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(null)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.create(board.accessId, req, clientInfo)
        }
    }

    @ParameterizedTest(name = "Cannot write post({1}) for \"{0}\" board")
    @MethodSource("testCannotCreatePost")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoardId = board.accessId
        val req = CreatePostRequestBuilder.createRandom()

        // when:
        `when`(boardRepo.findByAccessId(targetBoardId)).thenReturn(board)

        // then:
        Assertions.assertThrows(expectedException.java) {
            sut.create(targetBoardId, req, clientInfo)
        }
    }

    @Test
    fun `post is created if request is valid`() {
        // given:
        val (clientInfo, _, post) = postPreconditions()
        val req = CreatePostRequestBuilder.createRandom()
        val board = post.board

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)

        // then:
        sut.create(board.accessId, req, clientInfo)

        // and:
        val postCaptor: KArgumentCaptor<Post> = argumentCaptor()
        verify(postRepo, times(1)).save(postCaptor.capture())
        val savedPost = postCaptor.firstValue

        // expect: "Post"
        assertThat(savedPost.title, `is`(req.title))
        assertThat(savedPost.contents, `is`(req.contents))
        assertThat(savedPost.mode, `is`(req.mode))

        // and:
        val attachmentCaptor: KArgumentCaptor<List<Attachment>> = argumentCaptor()
        verify(attachmentRepo, times(1)).saveAll(attachmentCaptor.capture())
        val savedAttachments = attachmentCaptor.firstValue

        // expect: "Attachments"
        assertThat(savedAttachments.size, `is`(req.attachments.size))
        req.attachments.forEachIndexed { i, r ->
            assertThat("#$i attachment name is different", savedAttachments[i].name, `is`(r.name))
            assertThat("#$i attachment mimeType is different", savedAttachments[i].mimeType, `is`(r.mimeType))
            assertThat("#$i attachment uri is different", savedAttachments[i].uri, `is`(r.uri))
        }
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotCreatePost(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardNotFoundException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", CannotCreatePostException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                )
            )
        }
    }
}

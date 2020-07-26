/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotCreatePostException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.service.post.CreatePostService
import com.github.fj.board.service.post.impl.CreatePostServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito
import test.com.github.fj.board.endpoint.v1.post.dto.CreatePostRequestBuilder
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import java.util.*
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class CreatePostServiceTest : AbstractPostServiceTestTemplate() {
    @Mock
    private lateinit var postRepo: PostRepository

    private lateinit var sut: CreatePostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = CreatePostServiceImpl(userRepo, boardRepo, postRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreatePostRequestBuilder.createRandom()

        // when:
        Mockito.`when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.create(UUID.randomUUID(), req, clientInfo)
        }
    }

    @Test
    fun `fail if board for given boardId is not present`() {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoardId = UUID.randomUUID()
        val req = CreatePostRequestBuilder.createRandom()

        // when:
        Mockito.`when`(boardRepo.findByAccessId(targetBoardId)).thenReturn(null)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.create(targetBoardId, req, clientInfo)
        }
    }

    @ParameterizedTest(name = "Cannot write post for \"{0}\" board")
    @MethodSource("testCannotCreatePost")
    fun `fail if board has some constraints`(constraint: String, board: Board) {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoardId = board.accessId
        val req = CreatePostRequestBuilder.createRandom()

        // when:
        Mockito.`when`(boardRepo.findByAccessId(targetBoardId)).thenReturn(board)

        // then:
        assertThrows<CannotCreatePostException> {
            sut.create(targetBoardId, req, clientInfo)
        }
    }

    fun `post is created if request is valid`() {
        // expect:
        // 1. post is created as requested
        // 2. post number is increased in board
        //
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotCreatePost(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                )
            )
        }
    }
}

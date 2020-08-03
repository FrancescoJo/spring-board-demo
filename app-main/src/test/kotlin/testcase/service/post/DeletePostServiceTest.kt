/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotDeletePostException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.post.DeletePostService
import com.github.fj.board.service.post.impl.DeletePostServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
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

        this.sut = DeletePostServiceImpl(userRepo, boardRepo, postRepo, attachmentRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {

    }

    @Test
    fun `fail if board for given boardId is not present`() {

    }

    @ParameterizedTest(name = "Cannot delete post({1}) for \"{0}\" board")
    @MethodSource("testCannotDeletePost")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {

    }

    @Test
    fun `fail if target post is not found`() {

    }

    @Test
    fun `fail if target post is not owned`() {

    }

    @Test
    fun `post is deleted if request is valid`() {

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
                )
            )
        }
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.reply.CannotCreateReplyException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.reply.CreateReplyService
import com.github.fj.board.service.reply.impl.CreateReplyServiceImpl
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
 * @since 04 - Aug - 2020
 */
class CreateReplyServiceTest : AbstractReplyServiceTestTemplate() {
    private lateinit var sut: CreateReplyService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = CreateReplyServiceImpl(userRepo, boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {

    }

    @Test
    fun `fail if target post is not found`() {

    }

    @ParameterizedTest(name = "Cannot update post({1}) for \"{0}\" board")
    @MethodSource("testCannotCreateReply")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {

    }

    @Test
    fun `fail if board is READ ONLY`() {

    }

    @Test
    fun `fail if reply is not allowed in post`() {

    }

    @Test
    fun `reply is created if request is valid`() {

    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotCreateReply(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardNotFoundException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", CannotCreateReplyException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                )
            )
        }
    }
}

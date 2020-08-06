/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.reply.CannotEditReplyException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.service.reply.UpdateReplyService
import com.github.fj.board.service.reply.impl.UpdateReplyServiceImpl
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
 * @since 06 - Aug - 2020
 */
class UpdateReplyServiceTest : AbstractReplyServiceTestTemplate() {
    private lateinit var sut: UpdateReplyService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = UpdateReplyServiceImpl(userRepo, boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if user is not authenticated`() {

    }

    @Test
    fun `fail if target reply is not found`() {

    }

    @ParameterizedTest(name = "Cannot update reply({1}) for \"{0}\" board")
    @MethodSource("testCannotUpdateReplyByBoard")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {

    }

    @Test
    fun `fail if reply is not allowed in post`() {
        val constraintName = "PostMode:REPLY_NOT_ALLOWED"
        val exception = CannotEditReplyException::class
        val postMode = PostMode.REPLY_NOT_ALLOWED

        `fail if reply mode is #replyMode`(constraintName, exception, postMode)
    }

    @Test
    fun `fail if reply is only once in post`() {
        val constraintName = "PostMode:REPLY_ONCE"
        val exception = CannotEditReplyException::class
        val postMode = PostMode.REPLY_ONCE

        `fail if reply mode is #replyMode`(constraintName, exception, postMode)
    }

    private fun `fail if reply mode is #replyMode`(
        constraint: String,
        expectedException: KClass<out Exception>,
        postMode: PostMode
    ) {

    }

    @Test
    fun `fail if reply is not owned`() {

    }

    @Test
    fun `reply is updated if request is valid`() {

    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotUpdateReplyByBoard(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardNotFoundException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", CannotEditReplyException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                )
            )
        }
    }
}

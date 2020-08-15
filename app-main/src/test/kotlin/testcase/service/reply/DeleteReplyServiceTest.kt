/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.reply.CannotDeleteReplyException
import com.github.fj.board.exception.client.reply.ReplyNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.service.reply.DeleteReplyService
import com.github.fj.board.service.reply.impl.DeleteReplyServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.`when`
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import test.com.github.fj.board.persistence.entity.reply.ReplyBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import testcase.service.post.AbstractPostServiceTestTemplate
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Aug - 2020
 */
class DeleteReplyServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: DeleteReplyService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = DeleteReplyServiceImpl(userRepo, boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if user is not authenticated`() {
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
    fun `fail if target reply is not found`() {
        // given:
        val (clientInfo, user) = prepareSelf()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)

        // then:
        assertThrows<ReplyNotFoundException> {
            sut.delete(UUID.randomUUID(), clientInfo)
        }
    }

    @Test
    fun `fail if reply is not owned`() {
        // given:
        val (clientInfo, user, post) = postPreconditions()
        val reply = ReplyBuilder.createRandomOf(post, UserBuilder.createRandom())

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(replyRepo.findByAccessId(reply.accessId)).thenReturn(reply)

        assertThrows<CannotDeleteReplyException> {
            sut.delete(reply.accessId, clientInfo)
        }
    }

    @Test
    fun `fail if reply mode of parent post is REPLY_NOT_ALLOWED`() {
        `fail if reply mode of parent post is #replyMode`("PostMode:REPLY_NOT_ALLOWED", PostMode.REPLY_NOT_ALLOWED)
    }

    @Test
    fun `fail if reply mode of parent post is REPLY_ONCE`() {
        `fail if reply mode of parent post is #replyMode`("PostMode:REPLY_ONCE", PostMode.REPLY_ONCE)
    }

    private fun `fail if reply mode of parent post is #replyMode`(constraint: String, postMode: PostMode) {
        // given:
        val (clientInfo, user, post) = postPreconditions()
        post.apply {
            this.mode = postMode
        }
        val reply = ReplyBuilder.createRandomOf(post, user)

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(replyRepo.findByAccessId(reply.accessId)).thenReturn(reply)

        // then:
        assertThrows<CannotDeleteReplyException>("Test error on constraint '$constraint'") {
            sut.delete(reply.accessId, clientInfo)
        }
    }

    @ParameterizedTest(name = "Cannot delete reply({1}) for \"{0}\" board")
    @MethodSource("testCannotDeleteReplyByBoard")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {
        // given:
        val (clientInfo, user) = prepareSelf()
        val post = PostBuilder.createRandomOf(board, user)
        val reply = ReplyBuilder.createRandomOf(post, user)

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(replyRepo.findByAccessId(reply.accessId)).thenReturn(reply)

        // then:
        Assertions.assertThrows(expectedException.java) {
            sut.delete(reply.accessId, clientInfo)
        }
    }

    @Test
    fun `reply is deleted if request is valid`() {
        // given:
        val (clientInfo, user, post) = postPreconditions()
        val reply = ReplyBuilder.createRandomOf(post, user)

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(replyRepo.findByAccessId(reply.accessId)).thenReturn(reply)

        // then:
        val actual = sut.delete(reply.accessId, clientInfo)

        // expect:
        assertTrue(actual)
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testCannotDeleteReplyByBoard(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "Status:CLOSED", BoardNotFoundException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.CLOSED)
                        .mode(BoardMode.FREE_STYLE)
                        .build()
                ),
                Arguments.of(
                    "Mode:READ_ONLY", CannotDeleteReplyException::class, BoardBuilder(BoardBuilder.createRandom())
                        .status(BoardStatus.NORMAL)
                        .mode(BoardMode.READ_ONLY)
                        .build()
                )
            )
        }
    }
}

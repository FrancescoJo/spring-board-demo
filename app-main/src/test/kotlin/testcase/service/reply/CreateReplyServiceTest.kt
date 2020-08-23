/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.exception.client.reply.CannotCreateReplyException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.service.reply.CreateReplyService
import com.github.fj.board.service.reply.impl.CreateReplyServiceImpl
import com.nhaarman.mockitokotlin2.any
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
import test.com.github.fj.board.endpoint.v1.reply.dto.CreateReplyRequestBuilder
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import testcase.service.post.AbstractPostServiceTestTemplate
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class CreateReplyServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: CreateReplyService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = CreateReplyServiceImpl(userRepo, boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if user is not authenticated`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreateReplyRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.create(UUID.randomUUID(), req, clientInfo)
        }
    }

    @Test
    fun `fail if target post is not found`() {
        // given:
        val (clientInfo, user) = prepareSelf()
        val req = CreateReplyRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)

        // then:
        assertThrows<PostNotFoundException> {
            sut.create(UUID.randomUUID(), req, clientInfo)
        }
    }

    @ParameterizedTest(name = "Cannot create reply({1}) for \"{0}\" board")
    @MethodSource("testCannotCreateReply")
    fun `fail if board has some constraints`(
        constraint: String,
        expectedException: KClass<out Exception>,
        board: Board
    ) {
        // given:
        val (clientInfo, user) = prepareSelf()
        val post = PostBuilder.createRandomOf(board, user)
        val req = CreateReplyRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        Assertions.assertThrows(expectedException.java) {
            sut.create(post.accessId, req, clientInfo)
        }
    }

    @Test
    fun `fail if reply is not allowed in post`() {
        // given:
        val (clientInfo, user, post) = postPreconditions()
        post.apply {
            this.mode = PostMode.REPLY_NOT_ALLOWED
        }
        val req = CreateReplyRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        assertThrows<CannotCreateReplyException> {
            sut.create(post.accessId, req, clientInfo)
        }
    }

    @Test
    fun `reply is created if request is valid`() {
        // given:
        val (clientInfo, user, post) = postPreconditions()
        val req = CreateReplyRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)
        `when`(postRepo.findByAccessId(post.accessId)).thenReturn(post)

        // then:
        val actual = sut.create(post.accessId, req, clientInfo)

        // and:
        verify(replyRepo, times(1)).save(any())

        // expect:
        assertThat(actual.postId, `is`(post.accessId))
        assertThat(actual.writerNickname, `is`(user.nickname))
        assertThat(actual.writerLoginName, `is`(user.authentication.loginName))
        assertThat(actual.edited, `is`(false))
        assertThat(actual.contents, `is`(req.content))
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

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.board.CloseBoardService
import com.github.fj.board.service.board.impl.CloseBoardServiceImpl
import com.github.fj.lib.time.utcNow
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
class CloseBoardServiceTest : AbstractBoardServiceTestTemplate() {
    private lateinit var sut: CloseBoardService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = CloseBoardServiceImpl(userRepo, boardRepo)
    }

    @Test
    fun `fail if there is no User but only authentication`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.close(UUID.randomUUID(), clientInfo)
        }
    }

    @Test
    fun `fail if there is no board with given access id`() {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoard = UUID.randomUUID()

        // when:
        `when`(boardRepo.findByAccessId(targetBoard)).thenReturn(null)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.close(UUID.randomUUID(), clientInfo)
        }
    }

    @Test
    fun `only owner of board can close it`() {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoard = UUID.randomUUID()
        val board = BoardBuilder.createRandom()

        // when:
        `when`(boardRepo.findByAccessId(targetBoard)).thenReturn(board)

        // then:
        assertThrows<UnauthorisedException> {
            sut.close(targetBoard, clientInfo)
        }
    }

    @Test
    fun `cannot close if board is already closed`() {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoard = UUID.randomUUID()
        val board = BoardBuilder(BoardBuilder.createRandom())
            .status(BoardStatus.CLOSED)
            .build()

        // when:
        `when`(boardRepo.findByAccessId(targetBoard)).thenReturn(board)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.close(targetBoard, clientInfo)
        }
    }

    @Test
    fun `board is closed if request is valid`() {
        // given:
        val (clientInfo, self) = prepareSelf()
        val board = BoardBuilder(BoardBuilder.createRandom())
            .creator(self)
            .build()

        // when:
        `when`(boardRepo.findByAccessId(board.accessId)).thenReturn(board)

        // then:
        assertTrue(sut.close(board.accessId, clientInfo))

        // and:
        val boardCaptor: KArgumentCaptor<Board> = argumentCaptor()
        verify(boardRepo, times(1)).save(boardCaptor.capture())
        val updatedBoard = boardCaptor.firstValue

        // expect:
        assertThat(updatedBoard.status, `is`(BoardStatus.CLOSED))
        assertThat(updatedBoard.modifiedDate, lessThanOrEqualTo(utcNow()))
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.service.board.UpdateBoardService
import com.github.fj.board.service.board.impl.UpdateBoardServiceImpl
import com.github.fj.board.vo.user.UserInfo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import test.com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequestBuilder
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
class UpdateBoardServiceTest : AbstractBoardServiceTestTemplate() {
    private lateinit var sut: UpdateBoardService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = UpdateBoardServiceImpl(userRepo, boardRepo)
    }

    @Test
    fun `fail if there is no User but only authentication`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = UpdateBoardRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.update(UUID.randomUUID(), req, clientInfo)
        }
    }

    @Test
    fun `fail if there is no board with given access id`() {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoard = UUID.randomUUID()
        val req = UpdateBoardRequestBuilder.createRandom()

        // when:
        `when`(boardRepo.findByAccessId(targetBoard)).thenReturn(null)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.update(UUID.randomUUID(), req, clientInfo)
        }
    }

    @Test
    fun `only owner of board can update it`() {
        // given:
        val (clientInfo, _) = prepareSelf()
        val targetBoard = UUID.randomUUID()
        val req = UpdateBoardRequestBuilder.createRandom()
        val board = BoardBuilder.createRandom()

        // when:
        `when`(boardRepo.findByAccessId(targetBoard)).thenReturn(board)

        // then:
        assertThrows<UnauthorisedException> {
            sut.update(targetBoard, req, clientInfo)
        }
    }

    @Test
    fun `board is updated if request is valid`() {
        // given:
        val (clientInfo, self) = prepareSelf()
        val targetBoardId = UUID.randomUUID()
        val req = UpdateBoardRequestBuilder.createRandom()
        val board = BoardBuilder(BoardBuilder.createRandom())
            .accessId(targetBoardId)
            .creator(self)
            .build()

        // when:
        `when`(boardRepo.findByAccessId(targetBoardId)).thenReturn(board)

        // then:
        val result = sut.update(targetBoardId, req, clientInfo)

        // expect:
        assertThat(result.accessId, `is`(targetBoardId))
        assertThat(result.accessId, `is`(board.accessId))
        assertThat(result.key, `is`(board.key))
        assertThat(result.name, `is`(req.name))
        assertThat(result.description, `is`(req.description))
        assertThat(result.createdDate, `is`(board.createdDate))
        assertThat(result.modifiedDate, greaterThanOrEqualTo(board.modifiedDate))
        assertThat(result.creator, `is`(UserInfo.from(board.creator)))
    }
}

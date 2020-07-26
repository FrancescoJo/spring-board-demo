/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.service.board.impl.GetBoardServiceImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.`when`
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardSingleServiceTest : AbstractBoardServiceTestTemplate() {
    private lateinit var sut: GetBoardService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = GetBoardServiceImpl(boardRepo)
    }

    @Test
    fun `fail if board is not found for given accessId`() {
        // given:
        val accessId = UUID.randomUUID()

        // when:
        `when`(boardRepo.findByAccessId(accessId)).thenReturn(null)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.getOne(accessId, null)
        }
    }

    @Test
    fun `not authenticated user can not access to members only board`() {
        // given:
        val board = randomBoardWithAccess(BoardAccess.MEMBERS_ONLY)
        val accessId = board.accessId

        // when:
        `when`(boardRepo.findByAccessId(accessId)).thenReturn(board)

        // then:
        assertThrows<BoardNotFoundException> {
            sut.getOne(accessId, null)
        }
    }

    @Test
    fun `not authenticated user can only access to public board`() {
        // given:
        val access = BoardAccess.PUBLIC
        val board = randomBoardWithAccess(access)
        val accessId = board.accessId

        // when:
        `when`(boardRepo.findByAccessId(accessId)).thenReturn(board)

        // then:
        val result = sut.getOne(accessId, null)

        // expect:
        assertThat(result.accessId, `is`(accessId))
        assertThat(result.access, `is`(access))
    }

    @Test
    fun `authenticated user can access to any board`() {
        // given:
        val access = BoardAccess.MEMBERS_ONLY
        val (clientInfo, _) = prepareSelf()
        val board = randomBoardWithAccess(access)
        val accessId = board.accessId

        // when:
        `when`(boardRepo.findByAccessId(accessId)).thenReturn(board)

        // then:
        val result = sut.getOne(accessId, clientInfo)

        // expect:
        assertThat(result.accessId, `is`(accessId))
        assertThat(result.access, `is`(access))
    }

    @ParameterizedTest(name = "authenticated user can access to {0} board")
    @EnumSource
    fun `authenticated user can access to any board`(access: BoardAccess) {
        // given:
        val (clientInfo, _) = prepareSelf()
        val board = randomBoardWithAccess(access)
        val accessId = board.accessId

        // when:
        `when`(boardRepo.findByAccessId(accessId)).thenReturn(board)

        // then:
        val result = sut.getOne(accessId, clientInfo)

        // expect:
        assertThat(result.accessId, `is`(accessId))
        assertThat(result.access, `is`(access))
    }
}

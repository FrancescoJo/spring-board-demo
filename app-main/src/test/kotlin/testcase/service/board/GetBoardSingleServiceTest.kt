/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.service.board.impl.GetBoardServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    fun `not authenticated user can not access to members only board`() {

    }

    fun `not authenticated user can only access to public board`() {

    }

    fun `authenticated user can access to public and members only board`() {

    }
}

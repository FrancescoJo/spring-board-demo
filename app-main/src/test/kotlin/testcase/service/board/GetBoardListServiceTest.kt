/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.service.board.impl.GetBoardServiceImpl
import org.junit.jupiter.api.BeforeEach

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardListServiceTest : AbstractBoardServiceTestTemplate() {
    private lateinit var sut: GetBoardService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = GetBoardServiceImpl(boardRepo)
    }

    fun `not authenticated user can only get list of public boards`() {

    }

    fun `authenticated user can get full list of public boards`() {

    }

    fun `board list is sorted by given criteria`() {

    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.lib.annotation.CallSuper
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import testcase.service.user.AbstractUserServiceTestTemplate

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
abstract class AbstractBoardServiceTestTemplate : AbstractUserServiceTestTemplate(), BoardAuthorisationMixin {
    @Mock
    override lateinit var boardRepo: BoardRepository

    @BeforeEach
    @CallSuper
    override fun setup() {
        super.setup()
    }

    protected fun randomBoardWithAccess(access: BoardAccess): Board = BoardBuilder(BoardBuilder.createRandom())
        .access(access)
        .build()
}

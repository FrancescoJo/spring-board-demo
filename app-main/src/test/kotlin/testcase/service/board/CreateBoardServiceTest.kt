/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.CreateBoardService
import com.github.fj.board.service.board.impl.CreateBoardServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class CreateBoardServiceTest {
    @Mock
    private lateinit var userRepo: UserRepository

    @Mock
    private lateinit var boardRepo: BoardRepository

    private lateinit var sut: CreateBoardService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = CreateBoardServiceImpl(userRepo, boardRepo)
    }

    fun `fail if there is no User but only authentication`() {

    }

    fun `fail if key is duplicated`() {

    }

    fun `warn if name is duplicated`() {

    }

    fun `board is created if request is valid`() {

    }
}

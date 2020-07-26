/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.lib.annotation.CallSuper
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations
import testcase.service.board.AbstractBoardServiceTestTemplate

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
abstract class AbstractPostServiceTestTemplate : AbstractBoardServiceTestTemplate() {
    @BeforeEach
    @CallSuper
    override fun setup() {
        MockitoAnnotations.initMocks(this)
    }
}

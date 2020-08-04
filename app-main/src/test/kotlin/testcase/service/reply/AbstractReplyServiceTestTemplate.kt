/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.lib.annotation.CallSuper
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import testcase.service.post.AbstractPostServiceTestTemplate

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
abstract class AbstractReplyServiceTestTemplate : AbstractPostServiceTestTemplate() {
    @Mock
    protected lateinit var replyRepo: ReplyRepository

    @BeforeEach
    @CallSuper
    override fun setup() {
        MockitoAnnotations.initMocks(this)
    }
}

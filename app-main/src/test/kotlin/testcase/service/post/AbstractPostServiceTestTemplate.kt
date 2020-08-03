/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.service.post.PostAccessMixin
import com.github.fj.board.service.post.PostServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.annotation.CallSuper
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import testcase.service.board.AbstractBoardServiceTestTemplate

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
abstract class AbstractPostServiceTestTemplate : AbstractBoardServiceTestTemplate(), PostServiceMixin, PostAccessMixin {
    @Mock
    override lateinit var postRepo: PostRepository

    @Mock
    protected lateinit var attachmentRepo: AttachmentRepository

    @BeforeEach
    @CallSuper
    override fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    protected fun postPreconditions(): Triple<ClientAuthInfo, User, Board> {
        val (clientInfo, user) = prepareSelf()
        return Triple(clientInfo, user, BoardBuilder.createRandom())
    }
}

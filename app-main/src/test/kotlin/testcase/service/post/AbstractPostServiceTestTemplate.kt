/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.service.post.PostAccessMixin
import com.github.fj.board.service.post.PostServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.annotation.CallSuper
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.persistence.entity.post.PostBuilder
import testcase.service.board.AbstractBoardServiceTestTemplate

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
abstract class AbstractPostServiceTestTemplate : AbstractBoardServiceTestTemplate(), PostServiceMixin, PostAccessMixin {
    @Mock
    override lateinit var postRepo: PostRepository

    @Mock
    override lateinit var replyRepo: ReplyRepository

    @Mock
    protected lateinit var attachmentRepo: AttachmentRepository

    @BeforeEach
    @CallSuper
    override fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    protected fun postPreconditions(): Triple<ClientAuthInfo, User, Post> {
        val (clientInfo, user) = prepareSelf()
        val board = BoardBuilder.createRandom()
        return Triple(clientInfo, user, PostBuilder.createRandomOf(board, user))
    }
}

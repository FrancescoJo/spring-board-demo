/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.service.post.UpdatePostService
import com.github.fj.board.service.post.impl.UpdatePostServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
class UpdatePostServiceTest : AbstractPostServiceTestTemplate() {
    @Mock
    private lateinit var postRepo: PostRepository

    @Mock
    private lateinit var attachmentRepo: AttachmentRepository

    private lateinit var sut: UpdatePostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = UpdatePostServiceImpl(userRepo, boardRepo, postRepo, attachmentRepo)
    }

    @Test
    fun `fail is user is not authenticated`() {

    }

    @Test
    fun `fail if board for given boardId is not present`() {

    }

    @Test
    fun `fail if board is closed, archived or read-only`() {

    }

    @Test
    fun `fail if target post is not found`() {

    }

    @Test
    fun `fail if target attachments for deletion are not found`() {

    }

    @Test
    fun `post is updated if request is valid`() {

    }
}

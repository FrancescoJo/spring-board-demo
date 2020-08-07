/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.reply

import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.service.reply.impl.GetRepliesServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
class GetRepliesServiceTest : AbstractReplyServiceTestTemplate() {
    private lateinit var sut: GetRepliesService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = GetRepliesServiceImpl(boardRepo, postRepo, replyRepo)
    }

    @Test
    fun `fail if parent post is not found`() {

    }

    @Test
    fun `fail if user is unauthenticated and post is in non-public board`() {

    }

    @Test
    fun `list is returned if there are replies in given post`() {

    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

import com.github.fj.board.service.post.GetPostService
import com.github.fj.board.service.post.impl.GetPostServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
class GetPostSingleServiceTest : AbstractPostServiceTestTemplate() {
    private lateinit var sut: GetPostService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = GetPostServiceImpl(userRepo, boardRepo, postRepo)
    }

    @Test
    fun `fail if target post is not found`() {

    }

    @Test
    fun `fail if user is unauthenticated and post is in non-public board`() {

    }

    @Test
    fun `post detail and latest replies are returned`() {

    }
}

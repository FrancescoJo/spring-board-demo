/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint.v1.reply

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.v1.reply.GetRepliesController
import com.github.fj.board.endpoint.v1.reply.GetRepliesControllerImpl
import com.github.fj.board.service.reply.GetRepliesService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
class GetRepliesControllerTest {
    @Mock
    private lateinit var authDetector: ControllerClientAuthInfoDetector

    @Mock
    private lateinit var svc: GetRepliesService

    private lateinit var sut: GetRepliesController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = GetRepliesControllerImpl(authDetector, svc)
    }

    fun `postId must be a valid UUID format`() {

    }

    fun `fetch criteria is set to default if no request parameters are given`() {

    }

    fun `page is defaulted to PAGE_LATEST if page is in illegal format or below 0`() {

    }

    fun `count is defaulted as DEFAULT_REPLY_FETCH_SIZE to MAXIMUM_REPLY_FETCH_SIZE if unexpected values are given`() {

    }
}

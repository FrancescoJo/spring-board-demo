/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint.v1.auth

import com.github.fj.board.endpoint.v1.auth.SignInController
import com.github.fj.board.endpoint.v1.auth.SignInControllerImpl
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.service.auth.SignInService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Jul - 2020
 */
class SignInControllerTest {
    @Mock
    private lateinit var svc: SignInService

    private lateinit var sut: SignInController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = SignInControllerImpl(svc)
    }

    @Test
    fun `unspecified platformType will cause IllegalRequestException`() {
        // given:
        val request = AuthenticationRequestBuilder(AuthenticationRequestBuilder.createRandom())
            .platformType(PlatformType.UNDEFINED)
            .build()
        val httpRequest = mock(HttpServletRequest::class.java)

        // when:
        assertThrows<IllegalRequestException> {
            sut.signIn(request, httpRequest)
        }
    }

    // All happy cases will be tested under Integration tests
}

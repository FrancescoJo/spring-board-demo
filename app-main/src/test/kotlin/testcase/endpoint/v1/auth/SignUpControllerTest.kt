/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint.v1.auth

import com.github.fj.board.endpoint.v1.auth.SignUpController
import com.github.fj.board.endpoint.v1.auth.SignUpControllerImpl
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.service.auth.SignUpService
import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.lib.util.getRandomAlphaNumericString
import de.skuzzle.semantic.Version
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpControllerTest {
    @Mock
    private lateinit var svc: SignUpService

    private lateinit var sut: SignUpController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = SignUpControllerImpl(svc)
    }

    @Test
    fun `unspecified platformType will cause IllegalRequestException`() {
        // given:
        // FIXME to utilise SignUpRequestBuilder
        val request = SignUpRequest(
            loginName = getRandomAlphaNumericString(8),
            password = ProtectedProperty(getRandomAlphaNumericString(12)),
            platformType = PlatformType.UNDEFINED,
            platformVersion = getRandomAlphaNumericString(32),
            appVersion = Version.COMPLIANCE
        )
        val httpRequest = mock(HttpServletRequest::class.java)

        // when:
        assertThrows<IllegalRequestException> {
            sut.signUp(request, httpRequest)
        }
    }

    // All happy cases will be tested under Integration tests
}

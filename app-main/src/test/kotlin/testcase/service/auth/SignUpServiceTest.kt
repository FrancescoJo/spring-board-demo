/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.appconfig.CodecConfig
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.exception.client.DuplicatedLoginNameException
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.SignUpService
import com.github.fj.board.service.auth.SignUpServiceImpl
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.ProtectedProperty
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.any
import de.skuzzle.semantic.Version
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.temporal.ChronoUnit
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpServiceTest {
    private val base62Encoder = CodecConfig().base62()

    @Mock
    private lateinit var authProps: AppAuthProperties

    @Mock
    private lateinit var authRepo: AuthenticationRepository

    @Mock
    private lateinit var authTokenMgr: AuthTokenManager

    private lateinit var sut: SignUpService

    @BeforeEach
    internal fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = SignUpServiceImpl(base62Encoder, authProps, authRepo, authTokenMgr)
    }

    @Test
    fun `signUp attempt with same loginName causes DuplicatedLoginNameException`() {
        // given:
        val loginName = "DUPLICATED"
        val request = SignUpRequest(
            loginName = loginName,
            password = ProtectedProperty(getRandomAlphaNumericString(16)),
            // Change to random later on except UNDEFINED
            platformType = PlatformType.UNDEFINED,
            platformVersion = getRandomAlphaNumericString(16),
            appVersion = Version.COMPLIANCE
        )
        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)

        // when:
        `when`(authRepo.findByLoginName(loginName)).thenReturn(Authentication())

        // then:
        assertThrows<DuplicatedLoginNameException> {
            sut.signUp(request, httpReq)
        }
    }

    @Test
    fun `successful signUp attempt is stored and backed as SignUpResult`() {
        // given:
        val request = SignUpRequest(
            loginName = getRandomAlphaNumericString(8),
            password = ProtectedProperty(getRandomAlphaNumericString(16)),
            // FIXME Change to random later on except UNDEFINED
            platformType = PlatformType.UNDEFINED,
            platformVersion = getRandomAlphaNumericString(16),
            appVersion = Version.COMPLIANCE
        )
        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)
        val mockAccessToken = getRandomAlphaNumericString(128)
        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS

        // when:
        `when`(httpReq.remoteAddr).thenReturn("localhost")
        `when`(authProps.authTokenAliveSecs).thenReturn(tokenLifespanSecs)
        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)
        `when`(authRepo.findByLoginName(anyString())).thenReturn(null)
        `when`(authTokenMgr.create(anyString(), anyString(), any())).thenReturn(
            FreshHttpAuthorizationToken(mockAccessToken, utcNow().plusSeconds(tokenLifespanSecs))
        )

        // then:
        val result = sut.signUp(request, httpReq)

        // expect:
        verify(authRepo, times(1)).save(any<Authentication>())

        assertThat(result.loginName, `is`(request.loginName))
        assertThat(result.accessToken, `is`(mockAccessToken))
        assertThat(
            result.accessTokenExpiresAfter.truncatedTo(ChronoUnit.SECONDS),
            lessThanOrEqualTo(utcNow().plusSeconds(tokenLifespanSecs).truncatedTo(ChronoUnit.SECONDS))
        )
        assertTrue(result.refreshToken.isNotEmpty())
        assertThat(
            result.refreshTokenExpiresAfter.truncatedTo(ChronoUnit.SECONDS),
            lessThanOrEqualTo(utcNow().plusDays(refreshTokenLifespanDays).truncatedTo(ChronoUnit.SECONDS))
        )
    }
}

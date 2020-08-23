/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.appconfig.CodecConfig
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.exception.client.auth.LoginNotAllowedException
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.SignUpService
import com.github.fj.board.service.auth.impl.SignUpServiceImpl
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.any
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
class SignUpServiceTest {
    private val base62Encoder = CodecConfig().base62()

    @Mock
    private lateinit var authTokenMgr: AuthTokenManager

    @Mock
    private lateinit var authProps: AppAuthProperties

    @Mock
    private lateinit var authRepo: AuthenticationRepository

    private lateinit var sut: SignUpService

    @BeforeEach
    internal fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = SignUpServiceImpl(
            authTokenMgr,
            base62Encoder,
            authProps,
            authRepo
        )
    }

    @Test
    fun `signUp attempt with same loginName causes DuplicatedLoginNameException`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq = mockLocalhostServletRequest()
        val clientInfo = request.createClientAuthInfoBy(httpReq)

        // when:
        `when`(authRepo.findByLoginName(anyString())).thenReturn(Authentication())

        // then:
        assertThrows<LoginNotAllowedException> {
            sut.signUp(request, clientInfo)
        }
    }

    @Test
    fun `successful signUp attempt is stored and backed as AuthenticationResult`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq = mockLocalhostServletRequest()
        val clientInfo = request.createClientAuthInfoBy(httpReq)
        val mockAccessToken = getRandomAlphaNumericString(128)
        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS

        // when:
        `when`(authProps.authTokenAliveSecs).thenReturn(tokenLifespanSecs)
        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)
        `when`(authRepo.findByLoginName(anyString())).thenReturn(null)
        `when`(authTokenMgr.create(anyString(), anyString(), any())).thenReturn(
            FreshHttpAuthorizationToken(mockAccessToken, utcNow().plusSeconds(tokenLifespanSecs))
        )

        // then:
        val result = sut.signUp(request, clientInfo)

        // expect:
        verify(authRepo, times(1)).save(any())

        assertThat(result.loginName, `is`(request.loginName))
        assertThat(result.accessToken, `is`(mockAccessToken))
        assertThat(result.accessTokenExpiresAfter, lessThanOrEqualTo(utcNow().plusSeconds(tokenLifespanSecs)))
        assertTrue(result.refreshToken.isNotEmpty())
        assertThat(result.refreshTokenExpiresAfter, lessThanOrEqualTo(utcNow().plusDays(refreshTokenLifespanDays)))
    }
}

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
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.SignInService
import com.github.fj.board.service.auth.impl.SignInServiceImpl
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.any
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
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
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Jul - 2020
 */
class SignInServiceTest {
    private val base62Codec = CodecConfig().base62()

    @Mock
    private lateinit var authTokenMgr: AuthTokenManager

    @Mock
    private lateinit var authProps: AppAuthProperties

    @Mock
    private lateinit var authRepo: AuthenticationRepository

    private lateinit var sut: SignInService

    @BeforeEach
    internal fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = SignInServiceImpl(authTokenMgr, base62Codec, authProps, authRepo)
    }

    @Test
    fun `an attempt without nonexistent loginName causes LoginNotAllowedException`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq = mockLocalhostServletRequest()
        val clientInfo = request.createClientAuthInfoBy(httpReq)

        // when:
        `when`(authRepo.findByLoginName(anyString())).thenReturn(null)

        // then:
        assertThrows<LoginNotAllowedException> {
            sut.signIn(request, clientInfo, utcNow())
        }
    }

    @Test
    fun `an attempt with wrong password causes LoginNotAllowedException`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq = mockLocalhostServletRequest()
        val clientInfo = request.createClientAuthInfoBy(httpReq)
        val mockAuthentication = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .password("")
            .build()

        // when:
        `when`(authRepo.findByLoginName(request.loginName)).thenReturn(mockAuthentication)

        // expect:
        assertThrows<LoginNotAllowedException> {
            sut.signIn(request, clientInfo, utcNow())
        }
    }

    @Test
    fun `successful signIn attempt updates old Authentication and backed as AuthenticationResult`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq = mockLocalhostServletRequest()
        val clientInfo = request.createClientAuthInfoBy(httpReq)
        val mockAuthentication = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .password(request.password.value)
            .build()
        val mockAccessToken = getRandomAlphaNumericString(128)
        val now = utcNow()

        // and:
        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS

        // when:
        `when`(authProps.authTokenAliveSecs).thenReturn(tokenLifespanSecs)
        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)
        `when`(authTokenMgr.create(anyString(), anyString(), any())).thenReturn(
            FreshHttpAuthorizationToken(mockAccessToken, utcNow().plusSeconds(tokenLifespanSecs))
        )
        `when`(authRepo.findByLoginName(request.loginName)).thenReturn(mockAuthentication)

        // then:
        val result = sut.signIn(request, clientInfo, now)

        // expect:
        with (mockAuthentication) {
            assertThat(refreshToken, `is`(base62Codec.decode(result.refreshToken.toByteArray())))
            assertThat(refreshTokenIssuedAt, `is`(now))
            assertThat(refreshTokenExpireAt, `is`(now.plusDays(refreshTokenLifespanDays)))
        }

        // and:
        verify(authRepo, times(1)).save(mockAuthentication)
    }
}

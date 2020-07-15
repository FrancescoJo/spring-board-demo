/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.appconfig.CodecConfig
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.exception.client.LoginNotAllowedException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.SignInService
import com.github.fj.board.service.auth.SignInServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.endpoint.v1.auth.dto.AuthenticationRequestBuilder
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Jul - 2020
 */
class SignInServiceTest {
    private val base62Encoder = CodecConfig().base62()

    @Mock
    private lateinit var authProps: AppAuthProperties

    @Mock
    private lateinit var authRepo: AuthenticationRepository

    @Mock
    private lateinit var authTokenMgr: AuthTokenManager

    private lateinit var sut: SignInService

    @BeforeEach
    internal fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = SignInServiceImpl(base62Encoder, authProps, authRepo, authTokenMgr)
    }

    @Test
    fun `an attempt without nonexistent loginName causes LoginNotAllowedException`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)

        // when:
        `when`(authRepo.findByLoginName(anyString())).thenReturn(null)

        // then:
        assertThrows<LoginNotAllowedException> {
            sut.signIn(request, httpReq)
        }
    }

    @Test
    fun `an attempt with wrong password causes LoginNotAllowedException`() {
        // given:
        val request = AuthenticationRequestBuilder.createRandom()
        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)
        val mockAuthentication = AuthenticationBuilder(AuthenticationBuilder.createRandom())
                        .password("")
                        .build()

        // when:
        `when`(authRepo.findByLoginName(request.loginName)).thenReturn(mockAuthentication)

        // expect:
        assertThrows<LoginNotAllowedException> {
            sut.signIn(request, httpReq)
        }
    }

    @Test
    fun `successful signIn attempt updates old Authentication and backed as AuthenticationResult`() {
//        // given:
//        val request = AuthenticationRequestBuilder.createRandom()
//        val mockAuthentication = AuthenticationBuilder(request.toAuthentication())
//        val timestamp = utcNow()
//
//        // when:
//        val result = sut.signIn(request, httpReq, timestamp)
//
//        // then:
//        verify(authRepo, times(1)).save(any<Authentication>())
//
//        verify(mockAuthentication, times(1)).lastActiveDate = timestamp
    }

//    private fun prepareSignIn(request: AuthRequest, savedAuth: Authentication) {
//        // given:
//        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)
//        val mockAccessToken = getRandomAlphaNumericString(128)
//        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
//        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS
//
//        // when:
//        `when`(httpReq.remoteAddr).thenReturn("localhost")
//        `when`(authTokenMgr.create(anyString(), anyString(), any()))
//            .thenReturn(
//                FreshHttpAuthorizationToken(mockAccessToken, utcNow().plusSeconds(tokenLifespanSecs))
//            )
//        `when`(authProps.authTokenAliveSecs).thenReturn(tokenLifespanSecs)
//        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)
//        `when`(authRepo.findByLoginName(anyString())).thenReturn(mockAuthentication)
//    }
//
//    private fun AuthenticationRequest.toAuthentication() {
//        return AuthenticationBuilder(AuthenticationBuilder.createRandom())
//            .loginName(loginName)
//            .password(password.value)
//            .platformType(platformType)
//            .platformVersion(platformVersion)
//            .appVersion(appVersion)
//            .build()
//    }
}

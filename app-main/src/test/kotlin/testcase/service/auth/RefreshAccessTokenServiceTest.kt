/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.appconfig.CodecConfig
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.exception.client.LoginNameNotFoundException
import com.github.fj.board.exception.client.RefreshTokenMismatchException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.RefreshAccessTokenService
import com.github.fj.board.service.auth.RefreshAccessTokenServiceImpl
import com.github.fj.board.vo.auth.ClientRequestInfo
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.any
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest
import test.endpoint.v1.auth.dto.RefreshTokenRequestBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class RefreshAccessTokenServiceTest {
    private val base62Codec = CodecConfig().base62()

    @Mock
    private lateinit var authTokenMgr: AuthTokenManager

    @Mock
    private lateinit var authProps: AppAuthProperties

    @Mock
    private lateinit var authRepo: AuthenticationRepository

    private lateinit var sut: RefreshAccessTokenService

    @BeforeEach
    internal fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = RefreshAccessTokenServiceImpl(authTokenMgr, base62Codec, authProps, authRepo)
    }

    @Test
    fun `fail if there is auth found for loginName`() {
        // given:
        val loginName = getRandomAlphaNumericString(8)
        val req = RefreshTokenRequestBuilder.createRandom()
        val clientInfo = createClientRequestInfoBy(loginName)
        val now = utcNow()

        // expect:
        assertThrows<LoginNameNotFoundException> {
            sut.refreshAuthToken(req, clientInfo, now)
        }
    }

    @Test
    fun `fail if refreshToken mismatches`() {
        // given:
        val loginName = getRandomAlphaNumericString(8)
        val req = RefreshTokenRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .loginName(loginName)
            .build()
        val clientInfo = createClientRequestInfoBy(loginName)
        val now = utcNow()

        // when:
        `when`(authRepo.findByLoginName(loginName)).thenReturn(mockAuth)

        // then:
        assertThrows<RefreshTokenMismatchException> {
            sut.refreshAuthToken(req, clientInfo, now)
        }
    }

    @Test
    fun `fail if given refreshToken is too old`() {
        // given:
        val loginName = getRandomAlphaNumericString(8)
        val req = RefreshTokenRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .loginName(loginName)
            .refreshTokenExpireAt(LOCAL_DATE_TIME_MIN)
            .refreshTokenIssuedAt(LOCAL_DATE_TIME_MIN)
            .build()

        val clientInfo = createClientRequestInfoBy(loginName)
        val now = utcNow()

        // when:
        `when`(authRepo.findByLoginName(loginName)).thenReturn(mockAuth)

        // then:
        assertThrows<RefreshTokenMismatchException> {
            sut.refreshAuthToken(req, clientInfo, now)
        }
    }

    @Test
    fun `success if refreshToken is matched`() {
        // given:
        val loginName = getRandomAlphaNumericString(8)
        val req = RefreshTokenRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .loginName(loginName)
            .refreshToken(req.oldRefreshToken.value.toByteArray())
            .build()
        val clientInfo = createClientRequestInfoBy(loginName)
        val now = utcNow()

        // and:
        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS

        // when:
        `when`(authRepo.findByLoginName(loginName)).thenReturn(mockAuth)
        `when`(authTokenMgr.create(anyString(), anyString(), any())).thenReturn(
            FreshHttpAuthorizationToken(getRandomAlphaNumericString(128), utcNow().plusSeconds(tokenLifespanSecs))
        )
        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)

        // then:
        val result = sut.refreshAuthToken(req, clientInfo, now)

        // expect:
        assertThat(result.refreshToken.toByteArray(), not(mockAuth.refreshToken))
        assertThat(result.refreshTokenExpiresAfter, greaterThanOrEqualTo(now))
    }

    private fun createClientRequestInfoBy(loginName: String) = ClientRequestInfo.create(
        loginName = loginName,
        httpReq = mockLocalhostServletRequest()
    )
}

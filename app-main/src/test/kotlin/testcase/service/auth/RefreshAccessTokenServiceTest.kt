/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.exception.client.auth.LoginNameNotFoundException
import com.github.fj.board.exception.client.auth.RefreshTokenMismatchException
import com.github.fj.board.service.auth.RefreshAccessTokenService
import com.github.fj.board.service.auth.impl.RefreshAccessTokenServiceImpl
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcNow
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import test.com.github.fj.board.endpoint.v1.auth.dto.RefreshTokenRequestBuilder
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
class RefreshAccessTokenServiceTest : AbstractAuthenticationTestTemplate() {
    private lateinit var sut: RefreshAccessTokenService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = RefreshAccessTokenServiceImpl(authTokenMgr, base62Codec, authProps, authRepo)
    }

    @Test
    fun `fail if there is no auth found for loginName`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = RefreshTokenRequestBuilder.createRandom()
        val now = utcNow()

        // expect:
        assertThrows<LoginNameNotFoundException> {
            sut.refreshAuthToken(req, clientInfo, now)
        }
    }

    @Test
    fun `fail if refreshToken mismatches`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = RefreshTokenRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .loginName(clientInfo.loginName)
            .build()
        val now = utcNow()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(mockAuth)

        // then:
        assertThrows<RefreshTokenMismatchException> {
            sut.refreshAuthToken(req, clientInfo, now)
        }
    }

    @Test
    fun `fail if given refreshToken is too old`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = RefreshTokenRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .loginName(clientInfo.loginName)
            .refreshTokenExpireAt(LOCAL_DATE_TIME_MIN)
            .refreshTokenIssuedAt(LOCAL_DATE_TIME_MIN)
            .build()

        val now = utcNow()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(mockAuth)

        // then:
        assertThrows<RefreshTokenMismatchException> {
            sut.refreshAuthToken(req, clientInfo, now)
        }
    }

    @Test
    fun `success if refreshToken is matched`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = RefreshTokenRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .loginName(clientInfo.loginName)
            .refreshToken(base62Codec.decode(req.oldRefreshToken.value.toByteArray()))
            .build()
        val now = utcNow()
        val oldRefreshToken = mockAuth.refreshToken

        // when:
        setupDefaultTokenGenerationStrategy(clientInfo, mockAuth, now)

        // then:
        val result = sut.refreshAuthToken(req, clientInfo, now)

        // expect:
        assertThat(mockAuth.refreshToken, not(oldRefreshToken))
        assertThat(result.refreshTokenExpiresAfter, greaterThanOrEqualTo(now))
    }
}

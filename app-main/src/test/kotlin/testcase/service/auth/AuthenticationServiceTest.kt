/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.github.fj.lib.util.getRandomPositiveLong
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
class AuthenticationServiceTest : AbstractAuthenticationTestTemplate() {
    private lateinit var sut: Authentication

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = Authentication()
    }

    @Test
    fun `creating refreshToken without loginName causes IllegalStateException`() {
        // expect:
        assertThrows<IllegalStateException> {
            sut.createRefreshToken(utcNow(), 0L)
        }
    }

    @Test
    fun `relative fields are filled after successful refreshToken creation`() = with(sut) {
        // given:
        val now = utcNow()
        val lifespan = getRandomPositiveLong(1L, AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS)
        this.loginName = getRandomAlphaNumericString(8)

        // when:
        createRefreshToken(now, lifespan)

        // then:
        MatcherAssert.assertThat(refreshToken.size, Matchers.`is`(Authentication.REFRESH_TOKEN_LENGTH_BYTES))
        MatcherAssert.assertThat(refreshTokenIssuedAt, Matchers.`is`(now))
        MatcherAssert.assertThat(refreshTokenExpireAt, Matchers.`is`(now.plusDays(lifespan)))
    }

    @Test
    fun `refreshToken validation would fail if checked too late`() = with(sut) {
        this.refreshTokenExpireAt = utcNow().minusDays(AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS)

        // given:
        val actual = validateRefreshToken("".toByteArray())

        // expect:
        assertFalse(actual)
    }

    @Test
    fun `refreshToken validation would fail if #old is undecodable base62 format`() = with(sut) {
        // given:
        val actual = validateRefreshToken("!@#$%^&*()".toByteArray())

        // expect:
        assertFalse(actual)
    }

    @Test
    fun `refreshToken validation would fail if #old mismatches saved one`() = with(sut) {
        // given:
        val fakeOld = base62Codec.encode(getRandomAlphaNumericString(16).toByteArray())

        // when:
        val actual = validateRefreshToken(fakeOld)

        // then:
        assertFalse(actual)
    }

    @Test
    fun `refreshToken validation would success if #old matches saved one`() = with(sut) {
        // given:
        val now = utcNow()
        val lifespan = getRandomPositiveLong(1L, AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS)
        this.loginName = getRandomAlphaNumericString(8)

        // when:
        createRefreshToken(now, lifespan)

        // then:
        val actual = validateRefreshToken(this.refreshToken)

        // expect:
        assertTrue(actual)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.component.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.board.component.auth.impl.JwtAuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.HttpAuthScheme
import com.github.fj.board.component.security.MaybeHttpAuthorizationToken
import com.github.fj.board.exception.client.AuthTokenException
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getRandomAlphaNumericString
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.appconfig.security.MockRsaKeyPairManager
import java.security.Security
import java.util.concurrent.TimeUnit

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
class JwtAuthTokenManagerTest {
    private val rsaKeyPairManager = MockRsaKeyPairManager(24)
    private val jsonMapper = ObjectMapper()

    @Mock
    private lateinit var authProperties: AppAuthProperties

    private lateinit var sut: JwtAuthTokenManager

    private var tokenIssuer: String = getRandomAlphaNumericString(16)
    private var tokenAliveSecs: Long = TimeUnit.HOURS.toSeconds(2)

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        `when`(authProperties.tokenIssuer).thenReturn(tokenIssuer)
        `when`(authProperties.authTokenAliveSecs).thenReturn(tokenAliveSecs)

        this.sut = JwtAuthTokenManager(
            authProperties,
            rsaKeyPairManager,
            jsonMapper
        )
    }

    @Test
    fun `created jwt object contains information and signed by latest RSA keypair`() {
        // given:
        val audience = getRandomAlphaNumericString(8)
        val subject = getRandomAlphaNumericString(8)
        val timestamp = utcNow()

        // when:
        val authToken = sut.create(audience, subject, timestamp)

        // then:
        val jwtObject = sut.parseToken(authToken)

        // expect:
        with(jwtObject) {
            assertThat(this.issuer, `is`(tokenIssuer))
            assertThat(this.subject, `is`(subject))
            assertThat(this.audience, `is`(audience))
            assertThat(this.expiration, lessThanOrEqualTo(timestamp.plusSeconds(tokenAliveSecs)))
            assertThat(this.issuedAt, lessThanOrEqualTo(timestamp))
            assertThat(this.notBefore, lessThanOrEqualTo(JwtAuthTokenManager.NOT_BEFORE_THAN))
        }
    }

    @Test
    fun `wrong token causes AuthTokenException`() {
        // given:
        val wrongToken = MaybeHttpAuthorizationToken(HttpAuthScheme.TOKEN, getRandomAlphaNumericString(128))

        // expect:
        assertThrows<AuthTokenException> { sut.validate(wrongToken) }
    }

    @Test
    fun `issuer mismatch causes AuthTokenException`() {
        // given:
        val wrongIssuer = "__TAMPERED_ISSUER__"

        // and:
        `when`(authProperties.tokenIssuer).thenReturn(wrongIssuer)

        // when: "Create a issuer tempered token"
        val tamperedToken = sut.create("aud", "sub", utcNow())

        // then: "roll back issuer"
        `when`(authProperties.tokenIssuer).thenReturn(tokenIssuer)

        // expect:
        assertThrows<AuthTokenException> { sut.validate(tamperedToken) }
    }

    @Test
    fun `issuedAt timestamp before than notBefore causes AuthTokenException`() {
        // given:
        val pastThanNotBefore = JwtAuthTokenManager.NOT_BEFORE_THAN.minusDays(1L)

        // when:
        val tamperedToken = sut.create("aud", "sub", pastThanNotBefore)

        // then:
        assertThrows<AuthTokenException> { sut.validate(tamperedToken) }
    }

    @Test
    fun `expired token causes AuthTokenException`() {
        // given:
        val timestamp = utcNow().minusSeconds(tokenAliveSecs)
            .minusMinutes(JwtAuthTokenManager.EXPIRY_TOLERANCE_CLOCK_SKEW_MINS)

        // when:
        val tamperedToken = sut.create("aud", "sub", timestamp)

        // then:
        assertThrows<AuthTokenException> { sut.validate(tamperedToken) }
    }

    @Test
    fun `good token will transformed into Authentication object after validate`() {
        // given:
        val authToken = sut.create("aud", "sub", utcNow())
        val jwtObject = sut.parseToken(authToken)

        // when:
        val authentication = sut.validate(authToken)

        // then:
        with(authentication) {
            assertThat(authorities, `is`(emptyList()))
            assertThat(name, `is`(jwtObject.audience))
            assertThat(credentials as String, `is`(authToken.credentials))
            assertThat(principal as HttpAuthScheme, `is`(authToken.principal))
            assertThat(details as String, `is`(jwtObject.subject))
        }
    }

    companion object {
        @BeforeAll
        @JvmStatic
        @Suppress("unused")
        fun beforeClass() {
            Security.addProvider(BouncyCastleProvider())
        }

        @AfterAll
        @JvmStatic
        @Suppress("unused")
        fun afterClass() {
            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        }
    }
}

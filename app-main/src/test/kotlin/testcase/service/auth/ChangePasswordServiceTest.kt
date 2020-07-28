/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.exception.client.auth.DuplicatedPasswordException
import com.github.fj.board.exception.client.auth.LoginNameNotFoundException
import com.github.fj.board.exception.client.auth.WrongPasswordException
import com.github.fj.board.service.auth.ChangePasswordService
import com.github.fj.board.service.auth.impl.ChangePasswordServiceImpl
import com.github.fj.lib.security.toSha1Bytes
import com.github.fj.lib.text.toHexString
import com.github.fj.lib.time.utcNow
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import test.com.github.fj.board.endpoint.v1.auth.dto.ChangePasswordRequestBuilder
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
class ChangePasswordServiceTest : AbstractAuthenticationTestTemplate() {
    private lateinit var sut: ChangePasswordService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = ChangePasswordServiceImpl(authTokenMgr, base62Codec, authProps, authRepo)
    }

    @Test
    fun `fail if there is no auth found for loginName`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder.createRandom()

        // expect:
        assertThrows<LoginNameNotFoundException> {
            sut.changePassword(req, clientInfo, utcNow())
        }
    }

    @Test
    fun `fail if old password mismatches`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder.createRandom()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(AuthenticationBuilder.createRandom())

        // then:
        assertThrows<WrongPasswordException> {
            sut.changePassword(req, clientInfo, utcNow())
        }
    }

    @Test
    fun `fail if new password is same to old one`() {
        // given:
        val defaultPassword = "__DEFAULT_PW__"
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder()
            .oldPassword(defaultPassword)
            .newPassword(defaultPassword)
            .build()

        // and:
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .password(defaultPassword.toSha1Bytes().toHexString())
            .build()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(mockAuth)

        // then:
        assertThrows<DuplicatedPasswordException> {
            sut.changePassword(req, clientInfo, utcNow())
        }
    }

    @Test
    fun `success if new password is good`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .password(req.oldPassword.value)
            .build()
        val now = utcNow()
        val oldRefreshToken = mockAuth.refreshToken

        // when:
        setupDefaultTokenGenerationStrategy(clientInfo, mockAuth, now)

        // then:
        val result = sut.changePassword(req, clientInfo, now)
        val hashedNewPw = sut.hash(req.newPassword)

        // expect:
        assertThat(mockAuth.password, `is`(hashedNewPw))
        verify(authRepo, times(1)).save(mockAuth)

        // and: "Access token is also updated"
        assertThat(mockAuth.refreshToken, not(oldRefreshToken))
        assertThat(result.refreshTokenExpiresAfter, Matchers.greaterThanOrEqualTo(now))
    }
}

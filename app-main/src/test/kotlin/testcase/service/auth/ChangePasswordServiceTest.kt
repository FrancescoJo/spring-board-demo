/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.exception.client.DuplicatedPasswordException
import com.github.fj.board.exception.client.LoginNameNotFoundException
import com.github.fj.board.exception.client.WrongPasswordException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.ChangePasswordService
import com.github.fj.board.service.auth.impl.ChangePasswordServiceImpl
import com.github.fj.lib.security.toSha1Bytes
import com.github.fj.lib.text.toHexString
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.vo.auth.ClientRequestInfoBuilder
import test.endpoint.v1.auth.dto.ChangePasswordRequestBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
class ChangePasswordServiceTest {
    @Mock
    private lateinit var authRepo: AuthenticationRepository

    private lateinit var sut: ChangePasswordService

    @BeforeEach
    internal fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = ChangePasswordServiceImpl(authRepo)
    }

    @Test
    fun `fail if there is no auth found for loginName`() {
        // given:
        val clientInfo = ClientRequestInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder.createRandom()

        // expect:
        assertThrows<LoginNameNotFoundException> {
            sut.changePassword(req, clientInfo)
        }
    }

    @Test
    fun `fail if old password mismatches`() {
        // given:
        val clientInfo = ClientRequestInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder.createRandom()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(AuthenticationBuilder.createRandom())

        // then:
        assertThrows<WrongPasswordException> {
            sut.changePassword(req, clientInfo)
        }
    }

    @Test
    fun `fail if new password is same to old one`() {
        // given:
        val defaultPassword = "__DEFAULT_PW__"
        val clientInfo = ClientRequestInfoBuilder.createRandom()
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
            sut.changePassword(req, clientInfo)
        }
    }

    @Test
    fun `success if new password is good`() {
        // given:
        val clientInfo = ClientRequestInfoBuilder.createRandom()
        val req = ChangePasswordRequestBuilder.createRandom()

        // and:
        val mockAuth = AuthenticationBuilder(AuthenticationBuilder.createRandom())
            .password(req.oldPassword.value)
            .build()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(mockAuth)

        // then:
        sut.changePassword(req, clientInfo)
        val hashedNewPw = sut.hash(req.newPassword)

        // expect:
        assertThat(mockAuth.password, `is`(hashedNewPw))
        verify(authRepo, times(1)).save(mockAuth)
    }
}

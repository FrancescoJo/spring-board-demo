/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.user

import com.github.fj.board.exception.client.auth.LoginNameNotFoundException
import com.github.fj.board.exception.client.user.NicknameAlreadyExistException
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.user.UserStatus
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.CreateUserService
import com.github.fj.board.service.user.impl.CreateUserServiceImpl
import com.github.fj.lib.time.utcNow
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.endpoint.v1.user.dto.CreateUserRequestBuilder
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
class CreateUserServiceTest {
    @Mock
    private lateinit var authRepo: AuthenticationRepository

    @Mock
    private lateinit var userRepo: UserRepository

    private lateinit var sut: CreateUserService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = CreateUserServiceImpl(userRepo, authRepo)
    }

    @Test
    fun `fail if there is no auth found for loginName`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreateUserRequestBuilder.createRandom()

        // expect:
        assertThrows<LoginNameNotFoundException> {
            sut.create(req, clientInfo)
        }
    }

    @Test
    fun `fail if requested nickname is already in use`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreateUserRequestBuilder.createRandom()
        val mockAuth = AuthenticationBuilder.createRandom()
        val mockUser = UserBuilder(UserBuilder.createRandom())
            .authentication(mockAuth)
            .build()

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(mockAuth)
        `when`(userRepo.findByNickname(req.nickname)).thenReturn(mockUser)

        // then:
        assertThrows<NicknameAlreadyExistException> {
            sut.create(req, clientInfo)
        }
    }

    @ParameterizedTest(name = "user is created if invitedBy \"{0}\"")
    @NullSource
    @ValueSource(strings = ["", "someLoginName"])
    fun `user is created if request is valid`(invitedBy: String?) {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreateUserRequestBuilder(CreateUserRequestBuilder.createRandom())
            .invitedBy(invitedBy)
            .build()
        val mockAuth = AuthenticationBuilder.createRandom()
        val now = utcNow()
        val inviter = if (invitedBy.isNullOrEmpty()) {
            null
        } else {
            UserBuilder.createRandom()
        }

        // when:
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(mockAuth)
        if (!invitedBy.isNullOrEmpty()) {
            `when`(userRepo.findByNickname(invitedBy)).thenReturn(inviter)
        }

        // then:
        sut.create(req, clientInfo)

        // and:
        val userCaptor: KArgumentCaptor<User> = argumentCaptor()
        verify(userRepo, times(1)).save(userCaptor.capture())
        val actualUser = userCaptor.firstValue

        // expect:
        with(actualUser) {
            assertThat(authentication, `is`(mockAuth))
            assertThat(nickname, `is`(req.nickname))
            assertThat(status, `is`(UserStatus.UNVERIFIED))
            assertThat(email, `is`(req.email ?: ""))
            assertThat(createdDate, greaterThanOrEqualTo(now))
            assertThat(createdIp, `is`(clientInfo.remoteAddr))
            assertThat(lastActiveDate, greaterThanOrEqualTo(now))
            assertThat(lastActiveIp, `is`(clientInfo.remoteAddr))
            assertThat(lastActivePlatformType, `is`(clientInfo.platformType))
            assertThat(lastActivePlatformVersion, `is`(clientInfo.platformVer))
            assertThat(lastActiveAppVersion, `is`(clientInfo.appVer))
            assertThat(this.invitedBy, `is`(inviter))
        }
    }
}

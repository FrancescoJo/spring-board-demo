/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.user

import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.service.user.UpdateUserService
import com.github.fj.board.service.user.impl.UpdateUserServiceImpl
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.endpoint.v1.user.dto.UpdateUserRequestBuilder
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class UpdateUserServiceTest : AbstractUserServiceTestTemplate() {
    private lateinit var sut: UpdateUserService

    @BeforeEach
    override fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = UpdateUserServiceImpl(userRepo)
    }

    @Test
    fun `fail if user with nickname is not found`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val targetNickname = getRandomAlphaNumericString(8)

        // when:
        `when`(userRepo.findByNickname(targetNickname)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.update(targetNickname, UpdateUserRequestBuilder.createRandom(), clientInfo)
        }
    }

    @Test
    fun `fail if authenticated user does not match target user with nickname`() {
        // given:
        val targetNickname = getRandomAlphaNumericString(8)
        val request = UpdateUserRequestBuilder.createRandom()
        val clientInfo = ClientAuthInfoBuilder.createRandom()

        // and:
        val targetUser = UserBuilder(UserBuilder.createRandom())
            .nickname(targetNickname)
            .build()
            .forceAccessible()
        val self = UserBuilder(UserBuilder.createRandom())
            .nickname("$targetNickname.")
            .build()
            .forceAccessible()

        // when:
        `when`(userRepo.findByNickname(targetNickname)).thenReturn(targetUser)
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(self)

        // then:
        assertThrows<UnauthorisedException> {
            sut.update(targetNickname, request, clientInfo)
        }
    }

    @Test
    fun `no write operation has been done if there are no changes`() {
        // given:
        val request = UpdateUserRequestBuilder.createRandom()
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val self = UserBuilder(UserBuilder.createRandom())
            .nickname(request.nickname)
            .email(request.email ?: "")
            .build()
            .forceAccessible()

        // when:
        `when`(userRepo.findByNickname(self.nickname)).thenReturn(self)
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(self)

        // then:
        val result = sut.update(self.nickname, request, clientInfo)

        // expect:
        verify(userRepo, times(0)).save(any())

        // and:
        assertThat(result.nickname, `is`(self.nickname))
        assertThat(result.status, `is`(self.status))
        assertThat(result.email, `is`(self.email))
        assertThat(result.createdDate, `is`(self.createdDate))
        assertThat(result.lastActiveDate, `is`(self.lastActiveDate))
    }

    @Test
    fun `success if authenticated user matches target user with nickname`() {
        // given:
        val request = UpdateUserRequestBuilder.createRandom()
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val self = UserBuilder.createRandom().forceAccessible()
        val now = utcNow()

        // when:
        `when`(userRepo.findByNickname(self.nickname)).thenReturn(self)
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(self)

        // then:
        val result = sut.update(self.nickname, request, clientInfo)

        // and:
        val userCaptor: KArgumentCaptor<User> = argumentCaptor()
        verify(userRepo, times(1)).save(userCaptor.capture())
        val actualUser = userCaptor.firstValue

        // expect: "Entity is updated"
        assertThat(actualUser.nickname, `is`(request.nickname))
        assertThat(actualUser.email, `is`(request.email ?: ""))
        assertThat(actualUser.lastActiveDate, greaterThanOrEqualTo(now))
        assertThat(actualUser.lastActiveIp, `is`(clientInfo.remoteAddr))
        assertThat(actualUser.lastActivePlatformType, `is`(clientInfo.platformType))
        assertThat(actualUser.lastActivePlatformVersion, `is`(clientInfo.platformVer))
        assertThat(actualUser.lastActiveAppVersion, `is`(clientInfo.appVer))

        // and: "Result holds effective changes"
        assertThat(result.nickname, `is`(request.nickname))
        assertThat(result.status, `is`(self.status))
        assertThat(result.email, `is`(request.email ?: ""))
        assertThat(result.createdDate, `is`(self.createdDate))
        assertThat(result.lastActiveDate, greaterThanOrEqualTo(now))
    }
}

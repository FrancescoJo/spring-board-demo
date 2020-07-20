/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.user

import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.GetUserService
import com.github.fj.board.service.user.impl.GetUserServiceImpl
import com.github.fj.board.vo.user.UserInfo
import com.github.fj.lib.util.getRandomAlphaNumericString
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
class GetUserServiceTest {
    @Mock
    private lateinit var userRepo: UserRepository

    private lateinit var sut: GetUserService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = GetUserServiceImpl(userRepo)
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
            sut.get(targetNickname, clientInfo)
        }
    }

    @Test
    fun `success if user with nickname is found`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val targetNickname = getRandomAlphaNumericString(8)
        val mockUser = UserBuilder(UserBuilder.createRandom())
            .nickname(targetNickname)
            .build()

        // when:
        `when`(userRepo.findByNickname(targetNickname)).thenReturn(mockUser)

        // then:
        val actual = sut.get(targetNickname, clientInfo)

        // expect:
        assertThat(actual, `is`(UserInfo.from(mockUser)))
    }
}

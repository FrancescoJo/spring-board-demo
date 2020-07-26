/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.user

import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.annotation.CallSuper
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import test.com.github.fj.board.persistence.entity.user.UserBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
abstract class AbstractUserServiceTestTemplate : UserServiceMixin {
    @Mock
    override lateinit var userRepo: UserRepository

    @BeforeEach
    @CallSuper
    protected open fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    protected fun prepareSelf(): Pair<ClientAuthInfo, User> {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val user = UserBuilder.createRandom()

        // when:
        Mockito.`when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(user)

        return clientInfo to user
    }
}
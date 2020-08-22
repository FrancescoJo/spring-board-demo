/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.exception.client.auth.CannotWithdrawException
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.auth.DeleteAccountService
import com.github.fj.board.service.auth.impl.DeleteAccountServiceImpl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Aug - 2020
 */
class DeleteAccountServiceTest : AbstractAuthenticationTestTemplate() {
    @Mock
    private lateinit var userRepo: UserRepository

    private lateinit var sut: DeleteAccountService

    @BeforeEach
    override fun setup() {
        super.setup()

        this.sut = DeleteAccountServiceImpl(authRepo, userRepo)
    }

    @Test
    fun `fail if there is no auth found for loginName`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()

        // when:
        assertThrows<CannotWithdrawException> {
            sut.delete(clientInfo)
        }
    }

    @Test
    fun `withdraw is successful even if user is not found`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()

        // when
        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(AuthenticationBuilder.createRandom())

        // then:
        val actual = sut.delete(clientInfo)

        // expect
        assertTrue(actual)
    }
}

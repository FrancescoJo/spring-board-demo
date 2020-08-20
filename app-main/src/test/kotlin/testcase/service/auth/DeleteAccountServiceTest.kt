/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.auth.DeleteAccountService
import com.github.fj.board.service.auth.impl.DeleteAccountServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

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

    }

    @Test
    fun `user status is may changed to withdrawn after withdraw`() {

    }

    /*
     * A kind of 'loginName invalidation' method, and strange way to sweeping information.
     * However an user story that reads
     * `deleting account causes every data related to authentication are swept as well`
     * for heavy users would be a massive work, thus it could be a premium feature and doing more simpler way.
     */
    @Test
    fun `auth loginName and user nickname is replaced to reserved pattern after withdraw`() {

    }
}

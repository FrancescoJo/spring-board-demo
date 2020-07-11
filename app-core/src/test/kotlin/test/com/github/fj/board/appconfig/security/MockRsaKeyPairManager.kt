/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.appconfig.security

import com.github.fj.board.appconfig.security.RsaKeyPairManager
import test.com.github.fj.board.component.property.MockAppAuthProperties
import test.com.github.fj.board.persistence.repository.auth.MockRsaKeyPairRepository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
internal class MockRsaKeyPairManager(
    private val rsaKeyAliveHours: Long,
    mockAuthProps: MockAppAuthProperties = MockAppAuthProperties(),
    mockRsaKeyPairRepo: MockRsaKeyPairRepository = MockRsaKeyPairRepository()
) : RsaKeyPairManager(mockAuthProps, mockRsaKeyPairRepo) {
    init {
        mockAuthProps.rsaKeyAliveHours = this.rsaKeyAliveHours
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.component.property

import com.github.fj.board.component.property.AppAuthProperties

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
class MockAppAuthProperties : AppAuthProperties {
    override var rsaKeyAliveHours: Long = 0L

    override var authTokenAliveSecs: Long = 0L

    override var refreshTokenAliveDays: Long = 0L

    override var tokenIssuer: String = ""
}

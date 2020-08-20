/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.auth

import testcase.v1.AuthTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Aug - 2020
 */
class WithdrawSpec extends AuthTestBase {
    def "an unauthenticated user cannot delete account"() {

    }

    def "authenticated user can delete account"() {

    }

    // Any authenticated operations will fail after withdraw
    def "fail to create a board after withdraw"() {

    }
}

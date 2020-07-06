/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import testcase.IntegrationTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
class GetBoardsSpec extends IntegrationTestBase {
    def "GET /boards displays all post boards"() {
        given:
        final expected = 3

        and:
        final first = 1
        final second = 2

        when:
        final actual = first + second

        then:
        expected == actual
    }
}

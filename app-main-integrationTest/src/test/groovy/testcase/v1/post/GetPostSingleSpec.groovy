/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import spock.lang.Unroll
import testcase.v1.ReplyTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Aug - 2020
 */
class GetPostSingleSpec extends ReplyTestBase {
    def "fail if target post is not found"() {

    }

    def "fail if postId is not in a UUID format"() {

    }

    @Unroll
    def "fail with #expectedException if board is in #boardStatus state"() {

    }

    def "post detail and latest replies are returned"() {

    }
}

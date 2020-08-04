/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.reply

import testcase.v1.ReplyTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class CreateReplySpec extends ReplyTestBase {
    def "fail if not authenticated"() {

    }

    def "fail if parent post does not exist"() {

    }

    def "fail with #expectedException if board is in #boardStatus state"() {

    }

    def "fail if board is READ ONLY"() {

    }

    def "fail if reply is not allowed in post"() {

    }

    def "fail if content is empty"() {

    }

    def "reply is created if request is valid"() {

    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import spock.lang.Unroll
import testcase.common.CreatedUser
import testcase.v1.PostTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
class DeletePostSpec extends PostTestBase {
    private CreatedUser self
    private BoardInfo board
    private PostDetailedInfo post

    def setup() {
        this.self = createRandomUser()
        this.board = createRandomBoardOf(self)
        this.post = createRandomPostOf(self, board)
    }

    def "fail if not authenticated"() {

    }

    def "fail if board for given boardId is not present"() {

    }

    @Unroll
    def "fail with #expectedException if board is in #boardStatus state"() {

    }

    def "fail if board for given boardId is in READ ONLY mode"() {

    }

    def "fail if target post is not found"() {

    }

    def "fail if target post is not owned"() {

    }

    def "post is deleted if request is valid"() {

    }
}

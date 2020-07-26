/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.post

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class CreatePostServiceTest {
    fun `fail is user is not authenticated`() {

    }

    fun `fail if user is not found`() {

    }

    fun `fail if board for given boardId is not present`() {

    }

    fun `fail if board for given boardId is not in NORMAL state`() {

    }

    fun `fail if board for given boardId is in READ ONLY mode`() {

    }

    fun `post is created if request is valid`() {
        // expect:
        // 1. post is created by request
        // 2. post number is increased in board
        //
    }
}

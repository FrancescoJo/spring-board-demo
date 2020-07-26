/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Oct - 2018
 */
object ApiPaths {
    const val HELLO = "/hello"

    /** Used by Spring default */
    const val ERROR = "/error"

    const val V_ALL = "/v*"
    const val V1 = "/v1"
    const val LATEST_VERSION = V1

    const val ACCOUNT = "$LATEST_VERSION/account"
    const val TOKEN = "$LATEST_VERSION/token"
    const val PASSWORD = "$LATEST_VERSION/password"

    const val USER = "$LATEST_VERSION/user"
    const val USER_NICKNAME = "$USER/{nickname}"

    const val BOARD = "$LATEST_VERSION/board"
    const val BOARD_ID = "$LATEST_VERSION/board/{boardId}"
    const val BOARDS = "$LATEST_VERSION/boards"

    const val BOARD_ID_POST = "$BOARD_ID/post"
    const val BOARD_ID_POST_ID = "$BOARD_ID/post/{postId}"
    const val BOARD_ID_POSTS = "$BOARD_ID/posts"
}

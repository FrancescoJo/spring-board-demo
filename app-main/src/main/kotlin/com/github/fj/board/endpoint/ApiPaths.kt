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

    const val V_ALL = "v*"
    const val V1 = "v1"
    const val LATEST_VERSION = V1

    const val AUTH = "$LATEST_VERSION/auth"

    const val BOARDS = "$LATEST_VERSION/boards"
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.security

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
enum class HttpAuthScheme(val key: String) {
    BASIC("Basic"),
    BEARER("Bearer"),
    TOKEN("Token"),
    UNDEFINED("");

    companion object {
        fun byTypeValue(value: String) = values().firstOrNull {
            it.key == value
        } ?: UNDEFINED
    }
}

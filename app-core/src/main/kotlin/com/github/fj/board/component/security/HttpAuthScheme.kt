/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
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

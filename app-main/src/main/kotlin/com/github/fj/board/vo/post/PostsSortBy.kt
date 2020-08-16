/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.post

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Aug - 2020
 */
enum class PostsSortBy(private val value: String) {
    NUMBER("n"),
    VIEWED_COUNT("v");

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value == src }
    }
}

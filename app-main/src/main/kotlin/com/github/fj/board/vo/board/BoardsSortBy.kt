/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.board

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
enum class BoardsSortBy(private val value: String) {
    KEY("k"),
    NAME("n"),
    POSTS_COUNT("pn"),
    CREATED_DATE("cd"),
    LAST_MODIFIED_DATE("md");

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value == src }
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board.dto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
enum class BoardsSortOrderBy(private val value: String) {
    DESCENDING("new"),
    ASCENDING("old");

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value == src }
    }
}

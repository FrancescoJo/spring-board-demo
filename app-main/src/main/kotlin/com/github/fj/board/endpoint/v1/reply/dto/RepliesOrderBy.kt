/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply.dto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
enum class RepliesOrderBy(private val value: String) {
    DESCENDING("new"),
    ASCENDING("old");

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value == src }
    }
}

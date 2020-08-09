/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply.dto

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
enum class RepliesSortBy(private val value: String) {
    NUMBER("n");

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value == src }
    }
}

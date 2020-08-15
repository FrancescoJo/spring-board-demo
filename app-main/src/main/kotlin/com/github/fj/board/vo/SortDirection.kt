/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo

import org.springframework.data.domain.Sort

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
enum class SortDirection(private val value: String, val direction: Sort.Direction) {
    DESCENDING("new", Sort.Direction.DESC),
    ASCENDING("old", Sort.Direction.ASC);

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value.equals(src, ignoreCase = true) }
    }
}

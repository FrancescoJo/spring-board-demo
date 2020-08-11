/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.common.dto

import org.springframework.data.domain.Sort

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
enum class SortDirectionRequest(private val value: String, val direction: Sort.Direction) {
    DESCENDING("new", Sort.Direction.DESC),
    ASCENDING("old", Sort.Direction.ASC);

    companion object {
        fun fromString(src: String?) = values().firstOrNull { it.value.equals(src, ignoreCase = true) }
    }
}

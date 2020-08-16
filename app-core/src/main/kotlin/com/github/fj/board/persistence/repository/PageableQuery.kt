/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository

import org.springframework.data.domain.Sort

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Aug - 2020
 */
interface PageableQuery {
    val sortOrder: List<Sort.Order>

    val offset: Int

    val fetchSize: Int

    companion object {
        private data class Impl(
            override val sortOrder: List<Sort.Order>,
            override val offset: Int,
            override val fetchSize: Int
        ) : PageableQuery

        fun create(sortOrder: List<Sort.Order>, offset: Int, fetchSize: Int): PageableQuery = Impl(
            sortOrder = sortOrder,
            offset = offset,
            fetchSize = fetchSize
        )
    }
}

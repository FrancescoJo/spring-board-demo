/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo

import org.springframework.data.domain.Sort

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Aug - 2020
 */
interface ContentsFetchCriteria<SORT> {
    val sortBy: SORT

    val sortDirection: Sort.Direction

    val page: Int

    val fetchSize: Int

    companion object {
        private data class Impl<SORT>(
            override val sortBy: SORT,
            override val sortDirection: Sort.Direction,
            override val page: Int,
            override val fetchSize: Int
        ) : ContentsFetchCriteria<SORT>

        fun <SORT> create(
            sortBy: SORT,
            sortDirection: Sort.Direction,
            page: Int,
            fetchSize: Int
        ): ContentsFetchCriteria<SORT> = Impl(
            sortBy = sortBy,
            sortDirection = sortDirection,
            page = page,
            fetchSize = fetchSize
        )
    }
}

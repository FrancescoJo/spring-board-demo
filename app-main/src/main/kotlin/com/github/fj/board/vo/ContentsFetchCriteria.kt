/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo

import com.github.fj.board.persistence.repository.PageableQuery
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import kotlin.math.max

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Aug - 2020
 */
interface ContentsFetchCriteria<SORT> {
    val sortBy: SORT

    val sortDirection: Sort.Direction

    /**
     * 1-based value
     */
    val page: Int

    /**
     * Be careful not to set a large number on this property.
     */
    val fetchSize: Int

    /**
     * @param totalCount must be specified if first sort direction is DESCENDING in order to calculate offset
     * properly, or defaulted to 0 if unspecified but the value is required.
     */
    fun toPageableQuery(
        totalCount: Long = TOTAL_SIZE_UNDEFINED,
        toPropertyFunc: (SORT) -> String
    ): PageableQuery {
        val nonZeroPage = max(1, page)
        val nonSubZeroFetchSize = if (fetchSize > FETCH_SIZE_SENTINEL) {
            LOG.warn("Fetch size is defaulted to $FETCH_SIZE_SENTINEL while creating PageableQuery")

            FETCH_SIZE_SENTINEL
        } else {
            max(0, fetchSize)
        }

        val sortOrder = listOf(Sort.Order(sortDirection, toPropertyFunc(sortBy)))
        // Same as AbstractPageRequest#getOffset: enhanced by overflow guard
        val offset = try {
            max(0, Math.multiplyExact(nonZeroPage - 1, nonSubZeroFetchSize) - 1)
        } catch (e: ArithmeticException) {
            Integer.MAX_VALUE
        }

        return PageableQuery.create(sortOrder, offset, nonSubZeroFetchSize)
    }

    companion object {
        const val PAGE_DEFAULT = 1
        const val TOTAL_SIZE_UNDEFINED = -1L
        const val FETCH_SIZE_SENTINEL = 100

        private val LOG = LoggerFactory.getLogger(ContentsFetchCriteria::class.java)

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

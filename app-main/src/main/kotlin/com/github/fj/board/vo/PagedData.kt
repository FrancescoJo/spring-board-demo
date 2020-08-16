/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface PagedData<T> {
    val page: Int

    val size: Int

    val totalCount: Long

    val data: List<T>

    companion object {
        private data class Impl<T>(
            override val page: Int,
            override val size: Int,
            override val totalCount: Long,
            override val data: List<T>
        ) : PagedData<T>

        fun <T> create(page: Int, size: Int, totalCount: Long, data: List<T>): PagedData<T> = Impl(
            page = page,
            size = size,
            totalCount = totalCount,
            data = data
        )
    }
}

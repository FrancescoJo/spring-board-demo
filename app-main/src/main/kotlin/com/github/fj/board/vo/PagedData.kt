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
    val offset: Long

    val totalCount: Long

    val data: List<T>

    companion object {
        private data class Impl<T>(
            override val offset: Long,
            override val totalCount: Long,
            override val data: List<T>
        ) : PagedData<T>

        fun <T> create(
            offset: Long,
            totalCount: Long,
            data: List<T>
        ): PagedData<T> = Impl(
            offset = offset,
            totalCount = totalCount,
            data = data
        )
    }
}

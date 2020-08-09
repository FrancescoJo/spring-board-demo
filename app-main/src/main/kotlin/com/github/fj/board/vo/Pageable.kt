/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface Pageable<T> {
    val offset: Long

    val totalCount: Long

    val data: List<T>

    companion object {
        private data class Impl<T>(
            override val offset: Long,
            override val totalCount: Long,
            override val data: List<T>
        ) : Pageable<T>

        fun <T> create(
            offset: Long,
            totalCount: Long,
            data: List<T>
        ): Pageable<T> = Impl(
            offset = offset,
            totalCount = totalCount,
            data = data
        )
    }
}

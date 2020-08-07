/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface Pagable<T> {
    val index: Long

    val count: Long

    val totalCount: Long

    val data: List<T>
}
/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository

import org.springframework.data.domain.Pageable

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Aug - 2020
 */
interface PageableQueryHelperMixin {
    fun Pageable.toOrderByClause(entityAlias: String = ""): String = sort.iterator().let { sorts ->
        val propertyPrefix = if (entityAlias.isEmpty()) {
            ""
        } else {
            "$entityAlias."
        }

        if (sorts.hasNext()) {
            "ORDER BY " + sorts.asSequence().joinToString {
                "$propertyPrefix${it.property} ${it.direction}"
            }
        } else {
            ""
        }
    }

    /*
     * AbstractPageRequest#getOffset does same work but type is different, and no overflow guard
     */
    fun Pageable.toFirstResultOffset() = try {
        Math.multiplyExact(pageNumber, pageSize)
    } catch (e: ArithmeticException) {
        Integer.MAX_VALUE
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Aug - 2020
 */
interface PageableQueryHelperMixin {
    fun PageableQuery.toOrderByClause(entityAlias: String = ""): String {
        val propertyPrefix = if (entityAlias.isEmpty()) {
            ""
        } else {
            "$entityAlias."
        }

        return if (sortOrder.isEmpty()) {
            ""
        } else {
            "ORDER BY " + sortOrder.joinToString { "$propertyPrefix${it.property} ${it.direction}" }
        }
    }
}

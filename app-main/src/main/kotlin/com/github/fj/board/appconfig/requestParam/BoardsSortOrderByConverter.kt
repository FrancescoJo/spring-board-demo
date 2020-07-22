/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.requestParam

import com.github.fj.board.endpoint.v1.board.dto.BoardsSortOrderBy
import org.springframework.core.convert.converter.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class BoardsSortOrderByConverter : Converter<String, BoardsSortOrderBy> {
    override fun convert(source: String?): BoardsSortOrderBy = BoardsSortOrderBy.fromString(source)
}

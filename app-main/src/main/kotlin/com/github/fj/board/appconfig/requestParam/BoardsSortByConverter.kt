/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.requestParam

import com.github.fj.board.endpoint.v1.board.dto.BoardsSortBy
import org.springframework.core.convert.converter.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class BoardsSortByConverter : Converter<String, BoardsSortBy> {
    override fun convert(source: String?): BoardsSortBy = BoardsSortBy.fromString(source)
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.appconfig.requestParam.BoardsSortByConverter
import com.github.fj.board.appconfig.requestParam.BoardsSortOrderByConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@Configuration
class RequestParamConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(BoardsSortByConverter())
        registry.addConverter(BoardsSortOrderByConverter())
    }
}

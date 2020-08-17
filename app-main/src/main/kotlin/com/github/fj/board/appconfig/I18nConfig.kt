/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.FixedLocaleResolver
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Aug - 2020
 */
@Configuration
class I18nConfig {
    @Bean
    fun localeResolver(): LocaleResolver {
        // Force english for Spring Security error messages
        return FixedLocaleResolver(Locale.UK)
    }
}

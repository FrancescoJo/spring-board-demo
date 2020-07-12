/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import io.seruco.encoding.base62.Base62
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
@Configuration
class CodecConfig {
    @Bean
    fun base62(): Base62 = Base62.createInstance()
}

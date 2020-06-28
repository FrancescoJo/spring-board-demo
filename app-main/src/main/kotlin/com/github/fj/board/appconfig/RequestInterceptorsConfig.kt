/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.component.interceptor.RequestLoggingInterceptor
import com.github.fj.board.endpoint.ApiPaths
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Aug - 2018
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestInterceptorsConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(RequestLoggingInterceptor())
            .addPathPatterns("${ApiPaths.V_ALL}/**")
    }
}

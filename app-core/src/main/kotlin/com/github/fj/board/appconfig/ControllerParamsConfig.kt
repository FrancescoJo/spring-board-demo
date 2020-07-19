/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.appconfig.controllerParam.ClientAuthInfoResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Nov - 2018
 */
@Configuration
class ControllerParamsConfig : WebMvcConfigurer {
    override fun addArgumentResolvers(argResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argResolvers.add(ClientAuthInfoResolver())
    }
}

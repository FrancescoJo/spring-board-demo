/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.fj.board.appconfig.jackson.JacksonMsgConverterModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
@Configuration
class JacksonMessageConverterConfig @Inject constructor(
    private val defaultObjMapper: ObjectMapper
) : WebMvcConfigurer {
    override fun configureMessageConverters(
        converters: MutableList<HttpMessageConverter<*>>?
    ): Unit = with(defaultObjMapper) {
        registerModule(KotlinModule())
        registerModule(jacksonMsgConverterModule())
    }

    @Bean
    fun jacksonMsgConverterModule() = JacksonMsgConverterModule()
}

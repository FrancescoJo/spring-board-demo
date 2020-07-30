/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentsDeserialiser
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
@Configuration
class MessageConverterConfig(private val defaultObjMapper: ObjectMapper) : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        UpdateAttachmentsDeserialiser.injectMapper(defaultObjMapper)
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.advice

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.endpoint.AbstractResponseDto
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * This class wraps all [com.fasterxml.jackson.databind.annotation.JsonSerialize] annotated values within [com.github.fj.board.endpoint.OkResponseDto].
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Oct - 2018
 */
@RestControllerAdvice
class ResponseDtoDecorator : ResponseBodyAdvice<Any> {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        val methodReturnType = returnType.method?.returnType ?: return false
        return methodReturnType.declaredAnnotations.any {
            it.annotationClass == JsonSerialize::class
        }
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        if (body == null) {
            return null
        }

        return AbstractResponseDto.ok(body)
    }
}

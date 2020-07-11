/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.endpoint.v1.auth.dto.SignUpResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
interface SignUpController {
    @RequestMapping(
        path = [ApiPaths.AUTH],
        method = [RequestMethod.POST]
    )
    fun signUp(@Valid @RequestBody req: SignUpRequest, httpServletReq: HttpServletRequest): SignUpResponse
}
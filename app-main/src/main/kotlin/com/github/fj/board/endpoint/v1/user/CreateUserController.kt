/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.user

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.user.dto.CreateUserRequest
import com.github.fj.board.endpoint.v1.user.dto.CreateUserResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
interface CreateUserController {
    @RequestMapping(
        path = [ApiPaths.ACCOUNT],
        method = [RequestMethod.POST]
    )
    fun create(@Valid @RequestBody req: CreateUserRequest, httpServletReq: HttpServletRequest): CreateUserResponse
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@RestController
internal class CreateUserControllerImpl : CreateUserController {
    override fun create(req: CreateUserRequest, httpServletReq: HttpServletRequest): CreateUserResponse {
        TODO("Not yet implemented")
    }
}

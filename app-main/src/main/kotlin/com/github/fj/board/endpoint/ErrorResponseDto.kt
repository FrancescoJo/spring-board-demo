package com.github.fj.board.endpoint

import com.github.fj.board.endpoint.ErrorResponseDto.Body

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2018
 */
data class ErrorResponseDto(override val body: Body) : AbstractResponseDto<Body>(Type.ERROR) {
    data class Body(val message: String, val cause: String)
}

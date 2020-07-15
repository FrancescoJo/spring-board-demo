/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.exception.client

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
open class AccessTokenExpiredException(
    override val message: String = "Access token is expired.",
    override val cause: Throwable? = null
) : AuthTokenException(message, cause)

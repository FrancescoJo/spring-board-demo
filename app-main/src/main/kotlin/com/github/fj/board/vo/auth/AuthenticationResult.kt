/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.auth

import java.time.LocalDateTime

/**
 * An intermediate value object to pass results of sign-up step.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
data class AuthenticationResult(
    val loginName: String,
    val accessToken: String,
    val accessTokenExpiresAfter: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpiresAfter: LocalDateTime
)

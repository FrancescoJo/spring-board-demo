/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.auth

import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.component.security.HttpAuthorizationToken
import com.github.fj.board.exception.client.AuthTokenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
interface AuthTokenManager {
    fun create(audience: String, subject: String, timestamp: LocalDateTime): FreshHttpAuthorizationToken

    @Throws(AuthTokenException::class)
    fun validate(token: HttpAuthorizationToken): Authentication

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(AuthTokenManager::class.java)
    }
}

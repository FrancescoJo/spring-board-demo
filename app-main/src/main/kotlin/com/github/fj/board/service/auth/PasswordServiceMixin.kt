/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.lib.security.toSha256Bytes
import com.github.fj.lib.util.ProtectedProperty

/**
 * Mix-in for creating and validating given password.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
interface PasswordServiceMixin {
    fun hash(password: ProtectedProperty<String>): ByteArray =
        password.value.toSha256Bytes()

    fun Authentication.isMatch(password: ProtectedProperty<String>): Boolean {
        val expectedPassword = hash(password)

        return this.password.contentEquals(expectedPassword)
    }
}

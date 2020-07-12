/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.auth

import java.util.*

/**
 * An intermediate value object to pass results of sign-up step.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
data class SignUpResult(
    val loginName: String,
    val accessToken: CharArray,
    val refreshToken: CharArray
) {
    /**
     * Must be called immediately after this value object is used and become ineffective.
     */
    fun clearAll() {
        accessToken.fill(0.toChar())
        refreshToken.fill(0.toChar())
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SignUpResult) {
            return false
        }

        if (this === other) {
            return true
        }

        return Objects.equals(loginName, other.loginName) &&
                accessToken.contentEquals(other.accessToken) &&
                refreshToken.contentEquals(other.refreshToken)
    }

    override fun hashCode(): Int {
        var result = loginName.hashCode()
        result = 31 * result + accessToken.contentHashCode()
        result = 31 * result + refreshToken.contentHashCode()
        return result
    }
}

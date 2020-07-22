/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.auth.impl

import com.github.fj.board.component.security.HttpAuthScheme
import com.github.fj.board.component.security.HttpAuthorizationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Nov - 2018
 */
internal class AuthenticationObjectImpl(
    private val jwtObject: JwtObject,
    private val token: HttpAuthorizationToken
) : Authentication {
    private var isAuthenticated = false

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = ArrayList()

    override fun getName(): String = jwtObject.audience

    override fun getCredentials(): String = token.credentials

    override fun getPrincipal(): HttpAuthScheme = token.principal

    override fun getDetails(): String = jwtObject.subject

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AuthenticationObjectImpl) {
            return false
        }

        if (this === other) {
            return true
        }

        return Objects.equals(jwtObject, other.jwtObject) &&
                Objects.equals(token, other.token) &&
                Objects.equals(isAuthenticated, other.isAuthenticated)
    }

    /**
     * Inefficient but inevitable since `isAuthenticated` is mutable
     */
    override fun hashCode(): Int = Objects.hash(jwtObject, token, isAuthenticated)

    override fun toString() = """${AuthenticationObjectImpl::class.simpleName}(
        |  jwtObject=$jwtObject,
        |  token=$token
        )""".trimMargin()
}

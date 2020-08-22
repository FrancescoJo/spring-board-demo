/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.auth

import com.github.fj.board.persistence.converter.auth.AuthenticationStatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.auth.AuthenticationStatus
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.Size

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Entity
@Table(name = "authentications")
class Authentication : AbstractIncrementalLockableEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Convert(converter = AuthenticationStatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: AuthenticationStatus = AuthenticationStatus.NORMAL

    @Column(name = "login_name", length = 32, nullable = false, columnDefinition = "VARCHAR(32)")
    @Size(min = LOGIN_NAME_SIZE_MIN, max = LOGIN_NAME_SIZE_MAX)
    var loginName: String = ""

    @Column(length = 32, nullable = false, columnDefinition = "VARBINARY(32)")
    var password: ByteArray = ByteArray(0)

    @Column(name = "refresh_token", nullable = false, columnDefinition = "BLOB")
    var refreshToken: ByteArray = ByteArray(0)

    @Column(name = "refresh_token_issued_at", nullable = false)
    var refreshTokenIssuedAt: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Column(name = "refresh_token_expire_at", nullable = false)
    var refreshTokenExpireAt: LocalDateTime = LOCAL_DATE_TIME_MIN

    @OneToOne(cascade = [CascadeType.ALL], optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    var user: User? = null

    override fun toString(): String = "Authentication(id=$id, " +
            "status=$status, " +
            "loginName='$loginName', " +
            "password='[PROTECTED]', " +
            "refreshToken=[PROTECTED], " +
            "refreshTokenIssuedAt=$refreshTokenIssuedAt, " +
            "refreshTokenExpireAt=$refreshTokenExpireAt, " +
            "version=$version, " +
            "user=${user?.id?.toString() ?: "<uninitialised>"})"

    companion object {
        /** 256 bits */
        const val REFRESH_TOKEN_LENGTH_BYTES = 32

        const val LOGIN_NAME_SIZE_MIN = 4
        const val LOGIN_NAME_SIZE_MAX = 16
        const val PASSWORD_SIZE = 40L
    }
}

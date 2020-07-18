/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.auth

import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import java.time.LocalDateTime
import javax.persistence.*
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
    lateinit var user: User

    override fun toString(): String = "Authentication(id=$id, " +
            "loginName='$loginName', " +
            "password='[PROTECTED]', " +
            "refreshToken=[PROTECTED], " +
            "refreshTokenIssuedAt=$refreshTokenIssuedAt, " +
            "refreshTokenExpireAt=$refreshTokenExpireAt, " +
            "version=$version, " +
            "user=${if (::user.isInitialized) {
                user.id.toString()
            } else {
                "<uninitialised>"
            }})"

    companion object {
        /** 256 bits */
        const val REFRESH_TOKEN_LENGTH_BYTES = 32

        const val LOGIN_NAME_SIZE_MIN = 4
        const val LOGIN_NAME_SIZE_MAX = 16
        const val PASSWORD_SIZE = 40L
    }
}

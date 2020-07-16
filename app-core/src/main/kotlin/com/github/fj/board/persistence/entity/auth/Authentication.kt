/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.auth

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.SemanticVersionConverter
import com.github.fj.board.persistence.converter.auth.PlatformTypeConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.getSecureRandomBytes
import java.net.InetAddress
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

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "created_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var createdIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Column(name = "last_active_date", nullable = false)
    var lastActiveDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "last_active_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var lastActiveIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Convert(converter = PlatformTypeConverter::class)
    @Column(name = "last_active_platform_type", length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var lastActivePlatformType: PlatformType = PlatformType.UNDEFINED

    @Column(name = "last_active_platform_version", nullable = false, columnDefinition = "CLOB")
    var lastActivePlatformVersion: String = ""

    @Convert(converter = SemanticVersionConverter::class)
    @Column(name = "last_active_app_version", length = 32, nullable = false, columnDefinition = "VARCHAR(32)")
    var lastActiveAppVersion: de.skuzzle.semantic.Version = de.skuzzle.semantic.Version.ZERO

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
            "createdDate=$createdDate, " +
            "createdIp=$createdIp, " +
            "lastActiveDate=$lastActiveDate, " +
            "lastActiveIp=$lastActiveIp, " +
            "lastActivePlatformType=$lastActivePlatformType, " +
            "lastActivePlatformVersion='$lastActivePlatformVersion', " +
            "lastActiveAppVersion=$lastActiveAppVersion, " +
            "refreshToken=[PROTECTED], " +
            "refreshTokenIssuedAt=$refreshTokenIssuedAt, " +
            "refreshTokenExpireAt=$refreshTokenExpireAt, " +
            "version=$version, " +
            "user=${if (::user.isInitialized) {
                user.id.toString()
            } else {
                "<uninitialised>"
            }})"

    /**
     * Creates a refresh token for this authentication object and updates relative fields.
     *
     * @throws IllegalStateException if [loginName] is an empty string.
     */
    fun createRefreshToken(timestamp: LocalDateTime, lifespanDays: Long) {
        if (loginName.isEmpty()) {
            throw IllegalStateException("LoginName must be specified for this operation.")
        }

        this.refreshToken = getSecureRandomBytes(REFRESH_TOKEN_LENGTH_BYTES)
        this.refreshTokenIssuedAt = timestamp
        this.refreshTokenExpireAt = timestamp.plusDays(lifespanDays)
    }

    fun validateRefreshToken(oldToken: ByteArray): Boolean {
        // Rare cases
        if (utcNow() > refreshTokenExpireAt) {
            return false
        }

        return refreshToken.contentEquals(oldToken)
    }

    companion object {
        /** 256 bits */
        const val REFRESH_TOKEN_LENGTH_BYTES = 32

        const val LOGIN_NAME_SIZE_MIN = 4
        const val LOGIN_NAME_SIZE_MAX = 16
        const val PASSWORD_SIZE = 40L
    }
}

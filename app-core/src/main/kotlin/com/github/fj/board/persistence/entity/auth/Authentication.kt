/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.auth

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.PlatformTypeConverter
import com.github.fj.board.persistence.converter.SemanticVersionConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.PlatformType
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import java.net.InetAddress
import java.time.LocalDateTime
import javax.persistence.*

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
    var loginName: String = ""

    @Column(length = 128, nullable = false, columnDefinition = "VARBINARY(128)")
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

    @Column(name = "last_active_platform_version", length = 64, nullable = false)
    var lastActivePlatformVersion: String = ""

    @Convert(converter = SemanticVersionConverter::class)
    @Column(name = "last_active_app_version", length = 32, nullable = false, columnDefinition = "VARCHAR(32)")
    var lastActiveAppVersion = de.skuzzle.semantic.Version.ZERO

    @OneToOne(cascade = [CascadeType.ALL], optional = false, fetch = FetchType.EAGER)
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
            "version=$version, " +
            "user=${user.id})"
}

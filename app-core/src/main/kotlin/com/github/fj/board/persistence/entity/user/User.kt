/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.user

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.SemanticVersionConverter
import com.github.fj.board.persistence.converter.auth.PlatformTypeConverter
import com.github.fj.board.persistence.converter.user.UserStatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.persistence.model.user.UserStatus
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import java.net.InetAddress
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
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Entity
@Table(name = "users")
class User : AbstractIncrementalLockableEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    lateinit var authentication: Authentication

    @Column(length = 64, nullable = false, columnDefinition = "VARCHAR(64)")
    var nickname: String = ""

    @Convert(converter = UserStatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: UserStatus = UserStatus.UNVERIFIED

    @Column(length = 128, nullable = false, columnDefinition = "VARCHAR(128)")
    var email: String = ""

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

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "invited_by", nullable = true)
    var invitedBy: User? = null

    override fun toString() = "User(id=$id, " +
            "authentication=${if (::authentication.isInitialized) {
                "'${authentication.loginName}'"
            } else {
                "<uninitialised>"
            }} " +
            "nickname='$nickname', " +
            "status=$status, " +
            "email='$email', " +
            "createdDate=$createdDate, " +
            "createdIp=$createdIp, " +
            "lastActiveDate=$lastActiveDate, " +
            "lastActiveIp=$lastActiveIp, " +
            "lastActivePlatformType=$lastActivePlatformType, " +
            "lastActivePlatformVersion='$lastActivePlatformVersion', " +
            "lastActiveAppVersion=$lastActiveAppVersion, " +
            "invitedBy=${invitedBy?.let { "'${it.nickname}'" }})"
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.reply

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.converter.auth.PlatformTypeConverter
import com.github.fj.board.persistence.converter.post.ContentStatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.UuidExtensions
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
@Entity
@Table(name = "replies")
class Reply : AbstractIncrementalLockableEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "access_id", nullable = false, columnDefinition = "VARBINARY(16)")
    var accessId: UUID = UuidExtensions.EMPTY_UUID

    @Convert(converter = ContentStatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: ContentStatus = ContentStatus.NOT_REVIEWED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    lateinit var post: Post

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    lateinit var creator: User

    @Column(name = "last_modified_date", nullable = false)
    var lastModifiedDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "last_modified_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var lastModifiedIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Convert(converter = PlatformTypeConverter::class)
    @Column(name = "last_modified_platform_type", length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var lastModifiedPlatformType: PlatformType = PlatformType.UNDEFINED

    @Column(name = "edited", nullable = false, columnDefinition = "BIT")
    var edited: Boolean = false

    var number: Long = 0L

    @Column(columnDefinition = "CLOB")
    var contents: String = ""

    override fun toString() = "Reply(id=$id, " +
            "accessId=$accessId, " +
            "status=$status, " +
            "post=$post, " +
            "creator=$creator, " +
            "lastModifiedDate=$lastModifiedDate, " +
            "lastModifiedIp=$lastModifiedIp, " +
            "lastModifiedPlatformType=$lastModifiedPlatformType, " +
            "edited=$edited, " +
            "number=$number, " +
            "contents='$contents)"
}

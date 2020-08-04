/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.post

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.converter.auth.PlatformTypeConverter
import com.github.fj.board.persistence.converter.post.ContentStatusConverter
import com.github.fj.board.persistence.converter.post.PostModeConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.model.post.PostMode
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
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2020
 */
@Entity
@Table(name = "posts")
class Post : AbstractIncrementalLockableEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "access_id", nullable = false, columnDefinition = "VARBINARY(16)")
    var accessId: UUID = UuidExtensions.EMPTY_UUID

    @Convert(converter = ContentStatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: ContentStatus = ContentStatus.NOT_REVIEWED

    @Convert(converter = PostModeConverter::class)
    var mode: PostMode = PostMode.FREE_REPLY

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    lateinit var board: Board

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

    /*
     * Considering emoji input case, client-side length restrictions must be relatively shorter than 255.
     * i.e.) 'Flag of wales' ([U+1F3F4,󠁧 U+E0067, U+E0062, U+E0077, U+E006C, U+E0073, U+E007F]) emoji glyph is
     *       is consisted by 7 characters, so in this worst case title could hold only 36 glyphs (252 characters).
     */
    @Column(columnDefinition = "VARCHAR(255)")
    var title: String = ""

    @Column(columnDefinition = "CLOB")
    var contents: String = ""

    @Column(name = "viewed_cnt")
    var viewedCount: Long = 0L

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    var attachments: MutableList<Attachment> = mutableListOf()

    override fun toString() = "Post(id=$id, " +
            "accessId=$accessId, " +
            "status='$status', " +
            "mode='$mode', " +
            "board=${if (::board.isInitialized) {
                board.id.toString()
            } else {
                "<uninitialised>"
            }}, " +
            "creator=${creator.id}, " +
            "lastModifiedDate=$lastModifiedDate, " +
            "lastModifiedIp=$lastModifiedIp, " +
            "lastModifiedPlatformType=$lastModifiedPlatformType, " +
            "edited=$edited, " +
            "number=$number, " +
            "title='$title', " +
            "contents='${contents.trim()}', " +
            "viewedCount=$viewedCount, " +
            "attachments=$attachments)"
}

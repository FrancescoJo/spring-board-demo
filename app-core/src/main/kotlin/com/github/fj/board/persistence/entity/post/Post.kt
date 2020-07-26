/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.post

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.converter.SemanticVersionConverter
import com.github.fj.board.persistence.converter.auth.PlatformTypeConverter
import com.github.fj.board.persistence.converter.post.StatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.persistence.model.post.Status
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.UuidExtensions
import de.skuzzle.semantic.Version
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

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

    @Convert(converter = StatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: Status = Status.NOT_REVIEWED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    lateinit var board: Board

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    lateinit var user: User

    @ManyToOne(cascade = [CascadeType.ALL], optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_thread", nullable = true)
    var parentThread: Post? = null

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
    var lastActiveAppVersion: Version = Version.ZERO

    @Column(name = "edited", nullable = false, columnDefinition = "BIT")
    var edited: Boolean = false

    /*
     * Considering emoji input case, client-side length restrictions must be relatively shorter than 255.
     * i.e.) 'Flag of wales' ([U+1F3F4,󠁧 U+E0067, U+E0062, U+E0077, U+E006C, U+E0073, U+E007F]) emoji glyph is
     *       is consisted by 7 characters, so in this worst case title could hold only 36 glyphs (252 characters).
     */
    @Column(columnDefinition = "VARCHAR(255)")
    var title: String = ""

    @Column(columnDefinition = "CLOB")
    var contents: String = ""

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    var attachments: MutableList<Attachment> = mutableListOf()

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    var reactions: MutableList<Reaction> = mutableListOf()

    override fun toString() = "Post(id=$id, " +
            "accessId=$accessId, " +
            "status='$status', " +
            "board=${if (::board.isInitialized) {
                board.id.toString()
            } else {
                "<uninitialised>"
            }}, " +
            "user=${user.id}, " +
            "parentThread=${parentThread?.id?.toString()}, " +
            "lastActiveDate=$lastActiveDate, " +
            "lastActiveIp=$lastActiveIp, " +
            "lastActivePlatformType=$lastActivePlatformType, " +
            "lastActivePlatformVersion='$lastActivePlatformVersion', " +
            "lastActiveAppVersion=$lastActiveAppVersion, " +
            "edited=$edited, " +
            "title='$title', " +
            "contents='${contents.trim()}', " +
            "attachments=$attachments, " +
            "reactions=$reactions)"
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.post

import com.github.fj.board.persistence.converter.ByteArrayInetAddressConverter
import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.converter.post.StatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.post.ReactionKind
import com.github.fj.board.persistence.model.post.Status
import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.UuidExtensions
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

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_thread", nullable = true)
    var parentThread: Post? = null

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "created_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var createdIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

    @Column(name = "modified_date", nullable = false)
    var modifiedDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Convert(converter = ByteArrayInetAddressConverter::class)
    @Column(name = "modified_ip", nullable = false, columnDefinition = "VARBINARY(16)")
    var modifiedIp: InetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

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
            "createdDate=$createdDate, " +
            "createdIp=$createdIp, " +
            "modifiedDate=$modifiedDate, " +
            "modifiedIp=$modifiedIp, " +
            "title='$title', " +
            "contents='${contents.trim()}', " +
            "attachments=$attachments, " +
            "reactions=$reactions" +
            "version=$version)"
}

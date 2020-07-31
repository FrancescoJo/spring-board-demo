/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.board

import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.converter.board.BoardAccessConverter
import com.github.fj.board.persistence.converter.board.BoardModeConverter
import com.github.fj.board.persistence.converter.board.BoardStatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.UuidExtensions
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
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Entity
@Table(name = "boards")
class Board : AbstractIncrementalLockableEntity() {
    /**
     * Internal id, must not be exposed
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    /**
     * External id, could be exposed
     */
    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "access_id", nullable = false, columnDefinition = "VARBINARY(16)")
    var accessId: UUID = UuidExtensions.EMPTY_UUID

    @Convert(converter = BoardStatusConverter::class)
    var status: BoardStatus = BoardStatus.NORMAL

    @Convert(converter = BoardAccessConverter::class)
    var access: BoardAccess = BoardAccess.PUBLIC

    @Convert(converter = BoardModeConverter::class)
    var mode: BoardMode = BoardMode.FREE_STYLE

    /**
     * Alphanumeric, can be empty, human friendly name to specify a board
     */
    @Column(length = 16, nullable = false, columnDefinition = "VARCHAR(16)")
    var key: String = ""

    @Column(length = 128, nullable = false, columnDefinition = "VARCHAR(128)")
    var name: String = ""

    @Column(columnDefinition = "CLOB")
    var description: String = ""

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Column(name = "modified_date", nullable = false)
    var modifiedDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", nullable = false, updatable = false)
    lateinit var creator: User

    /*
     * We don't introduce this relation here since accessing to this field may require huge memory
     * when numbers Posts of this Board are sufficiently large
     */
    // @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    // lateinit var posts: MutableList<Post>

    override fun toString() = "Board(id=$id, " +
            "accessId=$accessId, " +
            "status=$status, " +
            "access=$access, " +
            "mode=$mode, " +
            "key='$key', " +
            "name='$name', " +
            "description='$description', " +
            "createdDate=$createdDate, " +
            "modifiedDate=$modifiedDate, " +
            "creator=${if (::creator.isInitialized) {
                creator.id.toString()
            } else {
                "<uninitialised>"
            }}" +
            "version=$version)"
}

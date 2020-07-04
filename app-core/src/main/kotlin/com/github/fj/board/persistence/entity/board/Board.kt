/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.board

import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.UuidExtensions
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Entity
@Table(name = "boards")
class Board : AbstractIncrementalLockableEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "access_id", nullable = false, columnDefinition = "VARBINARY(16)")
    var accessId: UUID = UuidExtensions.EMPTY_UUID

    @Column(length = 128, nullable = false, columnDefinition = "VARCHAR(128)")
    var name: String = ""

    @Column(name = "posts_count", nullable = false)
    var postsCount: Long = 0L

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    @Column(name = "modified_date", nullable = false)
    var modifiedDate: LocalDateTime = LOCAL_DATE_TIME_MIN

    /*
     * We don't introduce this relation here since accessing to this field may require huge memory
     * when numbers Posts of this Board are sufficiently large
     */
    // @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    // lateinit var posts: List<Post>

    override fun toString() = "Board(id=$id, " +
            "accessId=$accessId, " +
            "name='$name', " +
            "createdDate=$createdDate, " +
            "modifiedDate=$modifiedDate, " +
            "version=$version)"
}

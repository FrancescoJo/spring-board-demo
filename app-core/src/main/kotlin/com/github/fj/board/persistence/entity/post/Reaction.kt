/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.post

import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.board.persistence.converter.post.ReactionKindConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.model.post.ReactionKind
import com.github.fj.lib.util.UuidExtensions
import java.util.*
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2020
 */
@Entity
@Table(name = "post_reactions")
class Reaction : AbstractIncrementalLockableEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "access_id", nullable = false, columnDefinition = "VARBINARY(16)")
    var accessId: UUID = UuidExtensions.EMPTY_UUID

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    lateinit var post: Post

    @Convert(converter = ReactionKindConverter::class)
    @Column(nullable = false, columnDefinition = "VARCHAR(4)")
    lateinit var kind: ReactionKind

    /*
     * We don't introduce this relation here since accessing to this field may require huge memory
     * when numbers Reaction users of this Post are sufficiently large
     */
    // @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    // var users: List<PostReactionUser> = empyList()

    var count: Long = 0L

    override fun toString(): String {
        return "PostReaction(id=$id, " +
                "accessId=$accessId, " +
                "post=${if (::post.isInitialized) {
                    post.id.toString()
                } else {
                    "<uninitialised>"
                }}, " +
                "kind=$kind, " +
                "count=$count)"
    }
}

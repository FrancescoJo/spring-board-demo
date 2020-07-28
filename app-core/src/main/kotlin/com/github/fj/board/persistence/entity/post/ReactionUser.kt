/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.post

import com.github.fj.board.persistence.entity.user.User
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2020
 */
@Entity
@Table(name = "post_reactions_users")
class ReactionUser : Serializable {
    /**
     * Required for framework. Don't use this.
     */
    constructor()

    /**
     * Use this constructor for entity creation.
     */
    constructor(user: User) {
        this.user = user
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @ManyToOne
    @JoinColumn(name = "reaction_id", nullable = false, updatable = false)
    lateinit var reaction: Reaction

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    lateinit var user: User

    override fun toString(): String {
        return "PostReactionUser(id=$id, " +
                "reaction=${if (::reaction.isInitialized) {
                    reaction.id.toString()
                } else {
                    "<uninitialised>"
                }}, " +
                "user=$user)"
    }
}

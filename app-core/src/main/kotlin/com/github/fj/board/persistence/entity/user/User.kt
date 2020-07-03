/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.user

import com.github.fj.board.persistence.converter.user.GenderConverter
import com.github.fj.board.persistence.converter.user.StatusConverter
import com.github.fj.board.persistence.entity.Authentication
import com.github.fj.board.persistence.model.user.Gender
import com.github.fj.board.persistence.model.user.Status
import java.io.Serializable
import javax.persistence.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Entity
@Table(name = "users")
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    lateinit var authentication: Authentication

    @Column(length = 64, nullable = false)
    var nickname: String = ""

    @Convert(converter = StatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: Status = Status.UNVERIFIED

    @Column(length = 128, nullable = false, columnDefinition = "VARCHAR(128)")
    var email: String = ""

    /**
     * Invited user id
     * Not mapping directly to another 'User' object to prevent any chances of cyclic reference
     */
    @Column(name = "invited_by", nullable = false)
    var invitedBy: Long = 0L

    @Convert(converter = GenderConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var gender: Gender = Gender.UNDEFINED

    // For optimistic locking
    @Version
    private var version: Long = 0L

    override fun toString() = "User(id=$id, " +
            "authentication=<lazy> " +
            "nickname='$nickname', " +
            "status=$status, " +
            "email='$email', " +
            "invitedBy=$invitedBy " +
            "gender=$gender)"
}

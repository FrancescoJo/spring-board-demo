/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.user

import com.github.fj.board.persistence.converter.user.GenderConverter
import com.github.fj.board.persistence.converter.user.StatusConverter
import com.github.fj.board.persistence.entity.AbstractIncrementalLockableEntity
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.model.user.Gender
import com.github.fj.board.persistence.model.user.Status
import javax.persistence.*

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

    @Convert(converter = StatusConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var status: Status = Status.UNVERIFIED

    @Column(length = 128, nullable = false, columnDefinition = "VARCHAR(128)")
    var email: String = ""

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "invited_by", nullable = true)
    var invitedBy: User? = null

    @Convert(converter = GenderConverter::class)
    @Column(length = 4, nullable = false, columnDefinition = "VARCHAR(4)")
    var gender: Gender = Gender.UNDEFINED

    override fun toString() = "User(id=$id, " +
            "authentication=<lazy> " +
            "nickname='$nickname', " +
            "status=$status, " +
            "email='$email', " +
            "invitedBy=$invitedBy " +
            "gender=$gender)"
}

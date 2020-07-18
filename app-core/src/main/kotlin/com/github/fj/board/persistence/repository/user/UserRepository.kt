/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.user

import com.github.fj.board.persistence.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query(
        """
        SELECT u
        FROM User u
        WHERE u.nickname = ?1
    """
    )
    fun findByNickname(nickname: String): User?
}

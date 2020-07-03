/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository

import com.github.fj.board.persistence.entity.Authentication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Repository
interface AuthenticationRepository : JpaRepository<Authentication, Long> {
    @Query(
        """
        SELECT a
        FROM Authentication a
        WHERE a.loginName = ?1
    """
    )
    fun findByLoginName(loginName: String): Authentication?
}

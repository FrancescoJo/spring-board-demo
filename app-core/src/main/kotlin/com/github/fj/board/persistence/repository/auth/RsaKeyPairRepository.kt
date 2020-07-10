/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.auth

import com.github.fj.board.persistence.entity.auth.RsaKeyPair
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
interface RsaKeyPairRepository : JpaRepository<RsaKeyPair, UUID>, RsaKeyPairRepositoryExtension

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.repository.auth

import com.github.fj.board.persistence.entity.auth.RsaKeyPair
import com.github.fj.board.persistence.repository.auth.RsaKeyPairRepository
import com.github.fj.board.persistence.repository.auth.RsaKeyPairRepositoryExtension
import test.com.github.fj.board.persistence.MockJpaRepository
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
class MockRsaKeyPairRepository : MockJpaRepository<RsaKeyPair, UUID>(),
    RsaKeyPairRepository, RsaKeyPairRepositoryExtension {
    override fun findLatestOneYoungerThan(timeLimit: LocalDateTime): RsaKeyPair? {
        val allKeyPairs = super.findAll().apply {
            sortBy { it.issuedAt }
        }

        return allKeyPairs.firstOrNull {
            it.isEnabled && it.issuedAt >= timeLimit
        }
    }
}

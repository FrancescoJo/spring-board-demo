/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.repository.auth

import com.github.fj.board.persistence.entity.auth.RsaKeyPair
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.annotation.Nullable
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
interface RsaKeyPairRepositoryExtension {
    @Nullable
    fun findLatestOneYoungerThan(timeLimit: LocalDateTime): RsaKeyPair?
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
@Repository
internal class RsaKeyPairRepositoryExtensionImpl : RsaKeyPairRepositoryExtension {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Nullable
    override fun findLatestOneYoungerThan(timeLimit: LocalDateTime): RsaKeyPair? {
        val resultList = em.createQuery(
            """
                SELECT kp
                FROM RsaKeyPair kp
                WHERE kp.isEnabled = true
                  AND kp.issuedAt >= :timeLimit
                ORDER BY kp.issuedAt DESC
            """.trimIndent(), RsaKeyPair::class.java
        )
            .setParameter("timeLimit", timeLimit)
            .setMaxResults(1)
            .resultList

        return if (resultList.isEmpty()) {
            null
        } else {
            resultList.first()
        }
    }
}

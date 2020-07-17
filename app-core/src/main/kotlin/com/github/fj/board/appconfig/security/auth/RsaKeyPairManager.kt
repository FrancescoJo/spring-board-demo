/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.security.auth

import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.persistence.entity.auth.RsaKeyPair
import com.github.fj.board.persistence.repository.auth.RsaKeyPairRepository
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.time.utcNow
import com.github.fj.lib.util.FastCollectedLruCache
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * Does a little memory caching and DB lookup. Since generating and parsing RSA KeyPairs are quite
 * heavy job, therefore a little memory caching could be handy. However, due to its huge size,
 * a proper value must be set.
 *
 * The [RsaKeyPair] instance consumes approximately 4KiB of memory per instance with UTF-8 encoding.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
@Component
class RsaKeyPairManager @Inject constructor(
    authProps: AppAuthProperties,
    private val keyPairRepo: RsaKeyPairRepository
) {
    @VisibleForTesting
    internal var lruCache = FastCollectedLruCache.create<UUID, JwtRsaKeyPair>(
        LRU_CACHE_CAPACITY
    )

    @VisibleForTesting
    internal var pemHandler = PemKeyHandler()

    private var latestKeyPair: JwtRsaKeyPair? = null

    private val tokenLifetime = authProps.rsaKeyAliveHours

    // Early returns for case reduction
    @Suppress("ReturnCount")
    fun getLatest(): JwtRsaKeyPair {
        val now = utcNow()
        if (latestKeyPair.isValidAt(now)) {
            return requireNotNull(latestKeyPair)
        }

        synchronized(LATEST_ENTRY_WRITE_LOCK) {
            if (latestKeyPair == null) {
                keyPairRepo.findLatestOneYoungerThan(now.minusHours(tokenLifetime)).takeIf {
                    it != null
                }?.let {
                    return cache(deriveJwtRsaKeyPair(it))
                }

                val rawKeyPair = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME).run {
                    initialize(RSA_KEY_SIZE, SecureRandom())
                    return@run generateKeyPair()
                }

                val keyId = UUID.randomUUID()
                val publicKey = rawKeyPair.public as RSAPublicKey
                val privateKey = rawKeyPair.private as RSAPrivateKey
                saveRsaKeyPair(keyId, publicKey, privateKey, now)

                cache(deriveJwtRsaKeyPair(keyId, publicKey, privateKey, now.plusSeconds(tokenLifetime)))
            }

            return requireNotNull(latestKeyPair)
        }
    }

    fun getById(id: UUID): JwtRsaKeyPair {
        lruCache.get(id).takeIf { it != null }?.let { return it }

        keyPairRepo.findById(id).takeIf { it.isPresent }?.run { get() }?.let {
            with(deriveJwtRsaKeyPair(it)) {
                lruCache.put(keyId, this)
                return this
            }
        }

        throw IllegalArgumentException("Unknown JWT Key ID: $id")
    }

    fun invalidate(id: UUID) {
        val rsaKeyPair = keyPairRepo.findById(id).takeIf { it.isPresent }?.run { get() }?.apply {
            isEnabled = false
        } ?: return

        keyPairRepo.save(rsaKeyPair)
    }

    private fun JwtRsaKeyPair?.isValidAt(now: LocalDateTime): Boolean {
        if (this == null) {
            return false
        }

        return now < expiredAt
    }

    private fun cache(keyPair: JwtRsaKeyPair): JwtRsaKeyPair {
        lruCache.remove(keyPair.keyId)
        lruCache.put(keyPair.keyId, keyPair)
        latestKeyPair = keyPair

        return requireNotNull(latestKeyPair)
    }

    private fun deriveJwtRsaKeyPair(rsaKeyPair: RsaKeyPair): JwtRsaKeyPair = with(rsaKeyPair) {
        val rsaPubKey = pemHandler.fromPemFormat(publicKey) as RSAPublicKey
        val rsaPrivKey = pemHandler.fromPemFormat(privateKey) as RSAPrivateKey
        return@with deriveJwtRsaKeyPair(id, rsaPubKey, rsaPrivKey, issuedAt.plusSeconds(tokenLifetime))
    }

    private fun deriveJwtRsaKeyPair(
        keyId: UUID, publicKey: RSAPublicKey, privateKey: RSAPrivateKey, expiredAt: LocalDateTime
    ): JwtRsaKeyPair {
        return JwtRsaKeyPair(
            keyId,
            RSASSAVerifier(publicKey),
            RSASSASigner(privateKey),
            expiredAt
        )
    }

    private fun saveRsaKeyPair(keyId: UUID, pubKey: Key, privKey: Key, issuedAt: LocalDateTime) =
        keyPairRepo.save(RsaKeyPair().apply {
            id = keyId
            isEnabled = true
            publicKey = pemHandler.toPemFormat(pubKey)
            privateKey = pemHandler.toPemFormat(privKey)
            this.issuedAt = issuedAt
        })

    companion object {
        private val LATEST_ENTRY_WRITE_LOCK = Any()

        /** The cache will take approx. 300KiB(4KiB * 75 + SoftReference) of memory. */
        private const val LRU_CACHE_CAPACITY = 100
        private const val RSA_KEY_SIZE = 2048
    }
}

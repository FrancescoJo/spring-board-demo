/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.security.auth

import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
data class JwtRsaKeyPair(
    val keyId: UUID,

    /** Must be derived from public key */
    val rsaVerifier: RSASSAVerifier,

    /** Must be derived from private key */
    val rsaSigner: RSASSASigner,

    val expiredAt: LocalDateTime
)

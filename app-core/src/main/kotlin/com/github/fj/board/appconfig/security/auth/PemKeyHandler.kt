/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.security.auth

import com.github.fj.lib.annotation.VisibleForTesting
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import org.bouncycastle.util.io.pem.PemWriter
import java.io.StringReader
import java.io.StringWriter
import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Nov - 2018
 */
@VisibleForTesting
internal open class PemKeyHandler {
    open fun toPemFormat(key: Key): String = StringWriter().apply {
        PemWriter(this).use { it.writeObject(PemObject(key.format, key.encoded)) }
    }.toString()

    open fun fromPemFormat(pemKey: String): Key {
        return with(StringReader(pemKey)) {
            PemReader(this).use { it.readPemObject() }
        }.run {
            val keyFactory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME)
            return@run when (type) {
                FORMAT_PKCS8 -> keyFactory.generatePrivate(PKCS8EncodedKeySpec(content))
                FORMAT_X509 -> keyFactory.generatePublic(X509EncodedKeySpec(content))
                else         -> throw UnsupportedOperationException("$type type of key generation is not supported.")
            }
        }
    }

    companion object {
        private const val FORMAT_PKCS8 = "PKCS#8"
        private const val FORMAT_X509 = "X.509"
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.entity.auth

import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.persistence.converter.ByteArrayUuidConverter
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.util.UuidExtensions
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
@Entity
@Table(name = "rsa_key_pairs")
class RsaKeyPair {
    @Id
    @Convert(converter = ByteArrayUuidConverter::class)
    @Column(name = "uuid", nullable = false, columnDefinition = "VARBINARY(16)")
    var id: UUID = UuidExtensions.EMPTY_UUID

    /**
     * Setting to `false` means all information signed by this public key are must not be trusted.
     * Doing so will GUEST login users lose their credential and history - be careful!
     */
    @Column(name = "is_enabled", nullable = false, columnDefinition = "BIT")
    var isEnabled: Boolean = false

    /**
     * Do not use the private key for encryption! Only for signing!
     */
    @Column(name = "private_key", nullable = false, columnDefinition = "CLOB")
    var privateKey: String = ""

    /**
     * Use the public key for encryption and the private key for decryption!
     */
    @Column(name = "public_key", nullable = false, columnDefinition = "CLOB")
    var publicKey: String = ""

    /**
     * Any signing attempt after `issuedAt` + [AppAuthProperties.rsaKeyAliveHours]
     * hours must be refused. Still, any keys with this id, are can be verified with private key.
     */
    @Column(name = "issued_at")
    var issuedAt: LocalDateTime = LOCAL_DATE_TIME_MIN
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter

import com.github.fj.lib.util.toByteArray
import com.github.fj.lib.util.toUUID
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Jul - 2020
 */
@Converter
class ByteArrayUuidConverter : AttributeConverter<UUID, ByteArray> {
    override fun convertToDatabaseColumn(attribute: UUID?): ByteArray =
        attribute?.toByteArray() ?: throw IllegalArgumentException("UUID must not be null.")

    override fun convertToEntityAttribute(dbData: ByteArray?): UUID =
        dbData?.toUUID() ?: throw IllegalArgumentException("Cannot convert null to UUID.")
}

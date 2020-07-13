/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter

import de.skuzzle.semantic.Version
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class SemanticVersionConverter : AttributeConverter<Version, String> {
    override fun convertToDatabaseColumn(attribute: Version?): String =
        attribute?.toString() ?: Version.ZERO.toString()

    // All exception paths must be end up to same value
    @Suppress("TooGenericExceptionCaught")
    override fun convertToEntityAttribute(dbData: String?): Version = try {
        Version.parseVersion(dbData, true)
    } catch (e: RuntimeException) {
        Version.ZERO
    }
}

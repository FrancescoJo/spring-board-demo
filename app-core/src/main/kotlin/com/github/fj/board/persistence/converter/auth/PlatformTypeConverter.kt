/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.auth

import com.github.fj.board.persistence.model.auth.PlatformType
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Converter
class PlatformTypeConverter : AttributeConverter<PlatformType, String> {
    override fun convertToDatabaseColumn(attribute: PlatformType): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): PlatformType = PlatformType.byKey(dbData)
}

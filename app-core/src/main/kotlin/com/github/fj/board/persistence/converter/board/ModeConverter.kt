/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.board

import com.github.fj.board.persistence.model.board.Mode
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
@Converter
class ModeConverter : AttributeConverter<Mode, String> {
    override fun convertToDatabaseColumn(attribute: Mode): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): Mode = Mode.byKey(dbData)
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.board

import com.github.fj.board.persistence.model.board.Status
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@Converter
class StatusConverter : AttributeConverter<Status, String> {
    override fun convertToDatabaseColumn(attribute: Status): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): Status = Status.byKey(dbData)
}

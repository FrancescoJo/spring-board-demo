/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.user

import com.github.fj.board.persistence.model.user.Status
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Converter
class StatusConverter : AttributeConverter<Status, String> {
    override fun convertToDatabaseColumn(attribute: Status?): String =
        attribute?.key ?: Status.UNVERIFIED.key

    override fun convertToEntityAttribute(dbData: String?): Status =
        Status.byKey(dbData)
}

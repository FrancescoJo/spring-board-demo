/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.user

import com.github.fj.board.persistence.model.user.UserStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@Converter
class UserStatusConverter : AttributeConverter<UserStatus, String> {
    override fun convertToDatabaseColumn(attribute: UserStatus): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): UserStatus = UserStatus.byKey(dbData)
}

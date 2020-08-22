/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.auth

import com.github.fj.board.persistence.model.auth.AuthenticationStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2020
 */
@Converter
class AuthenticationStatusConverter : AttributeConverter<AuthenticationStatus, String> {
    override fun convertToDatabaseColumn(attribute: AuthenticationStatus): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): AuthenticationStatus = AuthenticationStatus.byKey(dbData)
}

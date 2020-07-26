/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.post

import com.github.fj.board.persistence.model.post.ReactionKind
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2020
 */
@Converter
class ReactionKindConverter : AttributeConverter<ReactionKind, String> {
    override fun convertToDatabaseColumn(attribute: ReactionKind): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): ReactionKind = ReactionKind.byKey(dbData)
}

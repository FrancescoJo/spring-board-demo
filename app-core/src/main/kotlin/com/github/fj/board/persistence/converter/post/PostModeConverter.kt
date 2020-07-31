/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.post

import com.github.fj.board.persistence.model.post.PostMode
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@Converter
class PostModeConverter : AttributeConverter<PostMode, String> {
    override fun convertToDatabaseColumn(attribute: PostMode): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): PostMode = PostMode.byKey(dbData)
}

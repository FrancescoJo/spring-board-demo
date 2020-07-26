/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.converter.post

import com.github.fj.board.persistence.model.post.PostStatus
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Jul - 2020
 */
@Converter
class StatusConverter : AttributeConverter<PostStatus, String> {
    override fun convertToDatabaseColumn(attribute: PostStatus): String = attribute.key

    override fun convertToEntityAttribute(dbData: String?): PostStatus = PostStatus.byKey(dbData)
}

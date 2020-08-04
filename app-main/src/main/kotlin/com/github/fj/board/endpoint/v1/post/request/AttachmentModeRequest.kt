/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import kotlin.reflect.KClass

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
enum class AttachmentModeRequest(val key: String, val expectedRequestClass: KClass<*>) {
    CREATE("+", CreateAttachmentRequest::class),
    DELETE("-", DeleteAttachmentRequest::class),
    UNDEFINED("", Nothing::class);

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun byKey(key: String?) = values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}

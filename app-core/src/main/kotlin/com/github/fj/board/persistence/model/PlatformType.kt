/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * Represents one of client's platform type.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
enum class PlatformType(val key: String) {
    ANDROID("a"),
    IOS("i"),
    WEB("w"),
    UNDEFINED("");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun byKey(key: String?) = values().firstOrNull { it.key == key } ?: UNDEFINED
    }
}

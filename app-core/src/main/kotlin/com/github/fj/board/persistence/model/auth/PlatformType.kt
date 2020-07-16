/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.model.auth

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * Represents one of client's platform type.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
enum class PlatformType(val key: String, val userAgentName: String) {
    ANDROID("a", "SpringBoard-Android"),
    IOS("i", "SpringBoard-iOS"),
    WEB("w", "SpringBoard-Web"),
    UNDEFINED("", "");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun byKey(key: String?) = values().firstOrNull { it.key == key } ?: UNDEFINED

        fun byUserAgentName(name: String?) = values().firstOrNull {
            it.userAgentName.equals(name, ignoreCase = true)
        } ?: UNDEFINED
    }
}

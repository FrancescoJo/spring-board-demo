/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.model.board

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
enum class Status(val key: String) {
    /**
     * Read & Write available
     */
    NORMAL("n"),

    /**
     * Read only, visible
     */
    READ_ONLY("r"),

    /**
     * Read only, invisible
     */
    ARCHIVED("x"),

    /**
     * Inaccessible
     */
    CLOSED("-");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun byKey(key: String?) = values().firstOrNull { it.key == key } ?: READ_ONLY
    }
}

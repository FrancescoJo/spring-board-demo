/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.persistence.model.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
enum class Status(val key: String) {
    UNVERIFIED("u"),
    VERIFIED("v"),
    SUSPENDED("s"),
    WITHDRAWN("w");

    @JsonValue
    @Suppress("unused") // Used by Jackson upon serialising @JsonSerialize annotated classes
    fun toValue(): String {
        return key
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun byKey(key: String?) = values().firstOrNull { it.key == key } ?: UNVERIFIED
    }
}

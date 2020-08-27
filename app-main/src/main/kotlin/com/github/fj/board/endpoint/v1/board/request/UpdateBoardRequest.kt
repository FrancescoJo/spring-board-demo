/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.board.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.validation.UnicodeCharsLength

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@JsonDeserialize
data class UpdateBoardRequest(
    @get:UnicodeCharsLength(
        min = NAME_SIZE_MIN,
        max = NAME_SIZE_MAX,
        message = "`name` must between $NAME_SIZE_MIN and $NAME_SIZE_MAX letters long."
    )
    @JsonProperty
    @JsonPropertyDescription(DESC_NAME)
    val name: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_DESCRIPTION)
    val description: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_ACCESS)
    val access: BoardAccess,

    @JsonProperty
    @JsonPropertyDescription(DESC_MODE)
    val mode: BoardMode
) {
    companion object {
        const val NAME_SIZE_MIN = 2
        const val NAME_SIZE_MAX = 18

        const val DESC_NAME = "A display name of this board. Should be concise and meaningful."
        const val DESC_DESCRIPTION = "Brief description of this board."
        const val DESC_ACCESS = "Access rights of board. Read link:#common-types-boardAccess[`BoardAccess`] " +
                "for more details."
        const val DESC_MODE = "Write mode of board. Read link:#common-types-boardMode[`BoardMode`] " +
                "for more details."
    }
}

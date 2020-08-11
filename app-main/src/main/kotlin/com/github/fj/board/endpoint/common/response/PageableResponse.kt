/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.common.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.vo.PagedData

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Aug - 2020
 */
@JsonSerialize
data class PageableResponse<T>(
    @JsonProperty
    @JsonPropertyDescription(DESC_OFFSET)
    val offset: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_TOTAL_COUNT)
    val totalCount: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_DATA)
    val data: List<T>
) {
    companion object {
        const val DESC_OFFSET = "Current offset of data list. You can fetch more data while " +
                "`offset` + `(size of data)` < `totalCount`."
        const val DESC_TOTAL_COUNT = "Total count of available data."
        const val DESC_DATA = "Actual data of windowed request."

        fun <T> create(offset: Long, totalCount: Long, data: List<T>) = PageableResponse(
            offset = offset,
            totalCount = totalCount,
            data = data
        )

        fun <T> from(src: PagedData<T>) = PageableResponse(
            offset = src.offset,
            totalCount = src.totalCount,
            data = src.data
        )
    }
}

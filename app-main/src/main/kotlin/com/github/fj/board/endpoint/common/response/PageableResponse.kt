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
    @JsonPropertyDescription(DESC_PAGE)
    val page: Int,

    @JsonProperty
    @JsonPropertyDescription(DESC_SIZE)
    val size: Int,

    @JsonProperty
    @JsonPropertyDescription(DESC_TOTAL_COUNT)
    val totalCount: Long,

    @JsonProperty
    @JsonPropertyDescription(DESC_DATA)
    val data: List<T>
) {
    companion object {
        const val DESC_PAGE = "Requested page of data list. Defaulted to '1' if unspecified."
        const val DESC_SIZE = "Requested size of data list. Note that this value may differ to size of `data` if " +
                "there are fewer data than requested size."
        const val DESC_TOTAL_COUNT = "Total count of available data."
        const val DESC_DATA = "Actual data of windowed request."

        fun <T> create(page: Int, size: Int, totalCount: Long, data: List<T>) = PageableResponse(
            page = page,
            size = size,
            totalCount = totalCount,
            data = data
        )

        fun <T> from(src: PagedData<T>) = PageableResponse(
            page = src.page,
            size = src.size,
            totalCount = src.totalCount,
            data = src.data
        )
    }
}

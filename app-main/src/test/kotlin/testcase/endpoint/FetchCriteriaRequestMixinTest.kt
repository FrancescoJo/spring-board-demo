/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint

import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin.Companion.GET_LIST_PARAM_COUNT
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin.Companion.GET_LIST_PARAM_ORDER_BY
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin.Companion.GET_LIST_PARAM_PAGE
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin.Companion.GET_LIST_PARAM_SORT_BY
import com.github.fj.board.vo.SortDirection
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.util.LinkedMultiValueMap
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
class FetchCriteriaRequestMixinTest : FetchCriteriaRequestMixin {
    @Test
    fun `sortBy is specified by passed 'sortsByProvider'`() {
        // given:
        val expected = "reply_count"
        val params = LinkedMultiValueMap<String, String>().apply {
            put(GET_LIST_PARAM_SORT_BY, listOf(expected))
        }

        // then:
        val actual = extractFetchCriteria(
            requestParams = params,
            sortByProvider = { expected },
            defaultSortDirection = SortDirection.DESCENDING,
            defaultPage = 0,
            defaultFetchSizeRange = 0..100
        )

        // expect:
        assertThat(actual.sortBy, `is`(expected))
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["", "__sort-dir__"])
    fun `sort direction is defaulted to 'defaultSortDirection' if not found in request`(sortDirection: String?) {
        // given:
        val expected = SortDirection.ASCENDING
        val params = LinkedMultiValueMap<String, String>().apply {
            put(GET_LIST_PARAM_ORDER_BY, listOf(sortDirection))
        }

        // then:
        val actual = extractFetchCriteria(
            requestParams = params,
            sortByProvider = { expected },
            defaultSortDirection = expected,
            defaultPage = 0,
            defaultFetchSizeRange = 0..100
        )

        // expect:
        assertThat(actual.sortDirection, `is`(expected.direction))
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["", "-1", "__page__"])
    fun `page is defaulted to 'defaultPage' if not found in request`(page: String?) {
        // given:
        val expected = Integer.MAX_VALUE
        val params = LinkedMultiValueMap<String, String>().apply {
            put(GET_LIST_PARAM_PAGE, listOf(page))
        }

        // then:
        val actual = extractFetchCriteria(
            requestParams = params,
            sortByProvider = { "SORTED_BY" },
            defaultSortDirection = SortDirection.DESCENDING,
            defaultPage = expected,
            defaultFetchSizeRange = 0..100
        )

        // expect:
        assertThat(actual.page, `is`(expected))
    }

    @ParameterizedTest(
        name = "count is defaulted as $FETCH_SIZE_MIN..$FETCH_SIZE_MAX if unexpected value('{0}') is given"
    )
    @MethodSource("testFetchSize")
    fun `fetchSize is defaulted to low or high end of 'defaultFetchSizeRange' if not found in request`(
        sizeReq: String?,
        expected: Int
    ) {
        // given:
        val params = LinkedMultiValueMap<String, String>().apply {
            put(GET_LIST_PARAM_COUNT, listOf(sizeReq))
        }

        // then:
        val actual = extractFetchCriteria(
            requestParams = params,
            sortByProvider = { "SORTED_BY" },
            defaultSortDirection = SortDirection.DESCENDING,
            defaultPage = expected,
            defaultFetchSizeRange = FETCH_SIZE_MIN..FETCH_SIZE_MAX
        )

        // expect:
        assertThat(actual.fetchSize, `is`(expected))
    }

    companion object {
        private const val FETCH_SIZE_MIN = 10
        private const val FETCH_SIZE_MAX = 20

        @JvmStatic
        @Suppress("unused")
        fun testFetchSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(null, FETCH_SIZE_MIN),
                Arguments.of("-1", FETCH_SIZE_MIN),
                Arguments.of("0", FETCH_SIZE_MIN),
                Arguments.of("${FETCH_SIZE_MIN - 1}", FETCH_SIZE_MIN),
                Arguments.of("$FETCH_SIZE_MIN", FETCH_SIZE_MIN),
                Arguments.of("${FETCH_SIZE_MAX - 1}", FETCH_SIZE_MAX - 1),
                Arguments.of("$FETCH_SIZE_MAX", FETCH_SIZE_MAX),
                Arguments.of("${FETCH_SIZE_MAX + 1}", FETCH_SIZE_MAX),
                Arguments.of("fetchSize", FETCH_SIZE_MIN)
            )
        }
    }
}

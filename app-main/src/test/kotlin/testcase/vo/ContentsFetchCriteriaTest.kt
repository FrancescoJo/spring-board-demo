/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.vo

import com.github.fj.board.vo.ContentsFetchCriteria
import com.github.fj.board.vo.ContentsFetchCriteria.Companion.PAGE_DEFAULT
import com.github.fj.lib.util.getRandomAlphaNumericString
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.domain.Sort
import test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Aug - 2020
 */
class ContentsFetchCriteriaTest {
    @ParameterizedTest(name = "fetchSize is defaulted to {1} for fetchSize={0}")
    @MethodSource("testNonZeroFetchSize")
    fun `fetchSize is defaulted to #expected value if value range is invalid`(fetchSize: Int, expected: Int) {
        // given:
        val sut = ContentsFetchCriteria.create(
            sortBy = getRandomAlphaNumericString(4),
            sortDirection = randomEnumConst(Sort.Direction::class.java),
            page = 0,
            fetchSize = fetchSize
        )

        // when:
        val actual = sut.toPageableQuery { it }

        // then:
        assertThat(actual.fetchSize, `is`(expected))
    }

    @Test
    fun `offset is defaulted to 0 if no totalCount is given for DESC sort`() {
        // given:
        val sut = ContentsFetchCriteria.create(
            sortBy = "prop",
            sortDirection = Sort.Direction.DESC,
            page = 0,
            fetchSize = 10
        )

        // when:
        val actual = sut.toPageableQuery { it }

        // then:
        assertThat(actual.offset, `is`(0))
    }

    @ParameterizedTest(name = "offset is defaulted to {2} per fetchSize={0} of totalCount={1}, ORDER BY DESC")
    @MethodSource("testOffsetForDesc")
    fun `offset is set to end for given totalCount with DESC sort`(
        page: Int,
        fetchSize: Int,
        totalCount: Long,
        expectedOffset: Int
    ) {
        // given:
        val sut = ContentsFetchCriteria.create(
            sortBy = "prop",
            sortDirection = Sort.Direction.DESC,
            page = page,
            fetchSize = fetchSize
        )

        // when:
        val actual = sut.toPageableQuery(totalCount) { it }

        // then:
        assertThat(actual.offset, `is`(expectedOffset))
    }

    @ParameterizedTest(name = "offset is defaulted to {2} per fetchSize={0} of totalCount={1}, ORDER BY ASC")
    @MethodSource("testOffsetForAsc")
    fun `offset is set by product of page and fetchSize with ASC sort`(
        page: Int,
        fetchSize: Int,
        totalCount: Long,
        expectedOffset: Int
    ) {
        // given:
        val sut = ContentsFetchCriteria.create(
            sortBy = "prop",
            sortDirection = Sort.Direction.ASC,
            page = page,
            fetchSize = fetchSize
        )

        // when:
        val actual = sut.toPageableQuery(totalCount) { it }

        // then:
        assertThat(actual.offset, `is`(expectedOffset))
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testNonZeroFetchSize(): Stream<Arguments> = Stream.of(
            // fetchSize, expected
            Arguments.of(-1, 0),
            Arguments.of(ContentsFetchCriteria.FETCH_SIZE_SENTINEL, ContentsFetchCriteria.FETCH_SIZE_SENTINEL),
            Arguments.of(ContentsFetchCriteria.FETCH_SIZE_SENTINEL + 1, ContentsFetchCriteria.FETCH_SIZE_SENTINEL)
        )

        @JvmStatic
        @Suppress("unused")
        fun testOffsetForDesc(): Stream<Arguments> = Stream.of(
            // page, fetchSize, totalCount, expectedOffset
            Arguments.of(PAGE_DEFAULT, 20, 31L, 0),
            Arguments.of(1, 20, 31L, 0),
            Arguments.of(2, 20, 31L, 19),
            Arguments.of(1, 20, 20L, 0)
        )

        @JvmStatic
        @Suppress("unused")
        fun testOffsetForAsc(): Stream<Arguments> = Stream.of(
            // page, fetchSize, totalCount, expectedOffset
            Arguments.of(PAGE_DEFAULT, 20, 31L, 0),
            Arguments.of(1, 20, 31L, 0),
            Arguments.of(2, 20, 31L, 19),
            Arguments.of(3, 20, 31L, 39)
        )
    }
}

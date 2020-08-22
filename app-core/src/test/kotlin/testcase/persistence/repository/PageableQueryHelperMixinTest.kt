/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.persistence.repository

import com.github.fj.board.persistence.repository.PageableQuery
import com.github.fj.board.persistence.repository.PageableQueryHelperMixin
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.domain.Sort
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Aug - 2020
 */
class PageableQueryHelperMixinTest : PageableQueryHelperMixin {
    @ParameterizedTest
    @MethodSource("testToOrderByClause")
    fun `orderBy clause is created by specified sort condition(s)`(prefix: String, sort: Sort, expected: String) {
        // given:
        val query = PageableQuery.create(sort.iterator().asSequence().toList(), 0, 0)

        // when:
        val actual = query.toOrderByClause(prefix)

        // expect:
        assertThat(actual, `is`(expected))
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testToOrderByClause(): Stream<Arguments> = Stream.of(
            // prefix, sort, expected
            Arguments.of("", Sort.unsorted(), ""),
            Arguments.of(
                "",
                Sort.by(Sort.Order(Sort.Direction.DESC, "property")),
                "ORDER BY property DESC"
            ),
            Arguments.of(
                "en",
                Sort.by(
                    Sort.Order(Sort.Direction.ASC, "p1"),
                    Sort.Order(Sort.Direction.DESC, "p2")
                ),
                "ORDER BY en.p1 ASC, en.p2 DESC"
            )
        )
    }
}

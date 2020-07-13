/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.util

import com.github.fj.lib.util.toArray
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2019
 */
class RangeUtilsTest {
    @Test
    fun `Integer range could be transformed as series of numbers`() {
        // given:
        val expected = arrayOf(1, 2, 3, 4, 5, 6, 7)
        val range = 1..7

        // when:
        val actual = range.toArray()

        // then:
        assertThat(actual.size, `is`(7))
        assertThat(actual, `is`(expected))
    }
}

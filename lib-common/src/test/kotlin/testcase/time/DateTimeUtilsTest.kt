/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.time

import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcLocalDateTimeOf
import com.github.fj.lib.time.utcNow
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.Test

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Mar - 2018
 */
class DateTimeUtilsTest {
    @Test
    fun `timestamp conversions demo`() {
        // given:
        val now = utcNow().withNano(0)

        // when:
        val longTimestamp = now.utcEpochSecond()
        val intTimestamp = longTimestamp.toInt()
        val systemEpoch = System.currentTimeMillis()

        // then:
        assertThat(utcLocalDateTimeOf(longTimestamp), greaterThanOrEqualTo(now))
        assertThat(utcLocalDateTimeOf(intTimestamp), greaterThanOrEqualTo(now))
        assertThat(utcLocalDateTimeOf(systemEpoch), greaterThanOrEqualTo(now))
    }
}

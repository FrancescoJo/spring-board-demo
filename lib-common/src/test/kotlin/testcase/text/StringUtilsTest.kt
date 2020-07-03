/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.text

import com.github.fj.lib.text.isNullOrUnicodeBlank
import com.github.fj.lib.text.matchesIn
import com.github.fj.lib.text.ynToBoolean
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.regex.Pattern
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Mar - 2018
 */
class StringUtilsTest {
    @Test
    fun `ynToBoolean yields expected result upon given input`() {
        val withInput: (String) -> Boolean = { it.ynToBoolean() }

        assertEquals(false, withInput("n"))
        assertEquals(false, withInput("N"))
        assertEquals(true , withInput("Y"))
        assertEquals(true , withInput("y"))
        assertEquals(false, withInput(""))
        assertEquals(true , withInput("aaaa"))
    }

    @Test
    fun `isUnicodeBlank yields expected result upon given input`() {
        val withInput: (String) -> Boolean = { it.isNullOrUnicodeBlank() }

        assertEquals(true, withInput(""))
        assertEquals(true, withInput("   "))
        assertEquals(false, withInput(" a "))
        assertEquals(false, withInput("\u3000 a \u3000"))
        assertEquals(true, withInput("\u3000\u3000"))
    }

    @ParameterizedTest(name = "\"{0}\".matchesIn(\"{1}\") ==> ''{2}''")
    @MethodSource("matchesInTestArgsProvider")
    fun `matchesIn test for various inputs`(str: String, pattern: Pattern, expected: Boolean) {
        assertEquals(expected, str.matchesIn(pattern))
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        private fun matchesInTestArgsProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("1234567890", Pattern.compile("""\d+"""), true),
                Arguments.of("AMZamz", Pattern.compile("""[A-Z]{3}[a-z]{3}"""), true),
                Arguments.of("1992Apd", Pattern.compile("""\d+$"""), false)
            )
        }
    }
}

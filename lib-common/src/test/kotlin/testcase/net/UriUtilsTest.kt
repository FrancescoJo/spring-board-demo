/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.net

import com.github.fj.lib.net.pathSegments
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.net.URI
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2018
 */
class UriUtilsTest {
    @ParameterizedTest(name = "\"{0}\" is separated into: {1}")
    @MethodSource("testSplitPathSegments")
    fun `Uris with slash will be separated correctly`(uriStr: String, expected: List<String>) {
        // given:
        val uri = URI.create(uriStr)

        // when:
        val actual = uri.pathSegments()

        // then:
        assertThat(actual, `is`(expected))
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testSplitPathSegments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("/", listOf("/")),
                Arguments.of("http://www.acme.com", emptyList<String>()),
                Arguments.of("http://www.acme.com/", listOf("/")),
                Arguments.of(
                    "http://www.acme.com/groups/00000000-0000-0000-0000-000000000000",
                    listOf("groups", "00000000-0000-0000-0000-000000000000")
                ),
                Arguments.of(
                    "http://www.acme.com/groups/00000000-0000-0000-0000-000000000000/",
                    listOf("groups", "00000000-0000-0000-0000-000000000000")
                )
            )
        }
    }
}

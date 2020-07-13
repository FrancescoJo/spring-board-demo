/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.text

import com.github.fj.lib.text.unicodeGraphemeCount
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2019
 */
class EmojiUtilsTest {
    @ParameterizedTest(name = "\"{1}\" glyph count must be: {0}")
    @MethodSource("testUnicodeGraphemeCountArgs")
    fun `unicodeGraphemeCount for various inputs`(length: Int, charSeq: String) {
        Assertions.assertEquals(length, charSeq.unicodeGraphemeCount())
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testUnicodeGraphemeCountArgs(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, ""),
                // Thai NFC case
                Arguments.of(28, "I 💚 Thai(บล็อกยูนิโคด) language"),
                // Emoji (7 characters for 1 glyph)
                Arguments.of(1, "🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
                // Chinese
                Arguments.of(14, "威爾士國旗看起來像： 🏴󠁧󠁢󠁷󠁬󠁳󠁿🏴󠁧󠁢󠁷󠁬󠁳󠁿🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
                // Japanese
                Arguments.of(10, "日本🇯🇵 大好き💚️!!"),
                // Korean NFD case
                Arguments.of(3, "\u1100\u1161\u1102\u1161\u1103\u1161")
            )
        }
    }
}

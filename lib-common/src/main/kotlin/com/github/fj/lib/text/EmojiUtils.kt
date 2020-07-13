/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import com.github.fj.lib.util.toArray
import com.vdurmont.emoji.EmojiParser
import java.text.BreakIterator

fun String.unicodeGraphemeCount(): Int {
    var count = 0
    forEachGlpyh { ++count }
    return count
}

fun String.forEachGlpyh(callback: (String) -> Unit) {
    EmojiIterator.iterateOn(this, callback)
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2019
 */
private object EmojiIterator : EmojiParser() {
    /**
     * [List of invisible Unicode characters](http://unicode.org/faq/unsup_char.html)
     *
     * See also: https://www.fileformat.info/info/unicode/category/Cf/list.htm
     */
    @Suppress("MagicNumber", "SpreadOperator")
    private val INVISIBLE_UNICODE_CHARS = setOf(
        0x00AD,                                         // Soft hyphen
        0x115F, 0x1160,                                 // Hangul Jamo fillers
        0x200B,                                         // Zero-width space
        0x200C, 0x200D,                                 // Cursive joiners
        0x200E, 0x200F, *(0x202A..0x202E).toArray(),    // Bidirectional format controls
        0x205F, *(0x2061..0x206F).toArray(),            // General punctuations
        *(0xFE00..0xFE0F).toArray(),                    // Variation selectors
        0x2060, 0xFEFF                                  // Word joiners
    )

    fun iterateOn(seq: String, callback: (String) -> Unit) {
        if (seq.isEmpty()) {
            return
        }

        val it = BreakIterator.getCharacterInstance().apply { setText(seq) }
        var idx = 0

        while (idx < seq.length) {
            val emojiEnd = getEmojiEndPos(seq.toCharArray(), idx)
            if (emojiEnd > -1) {
                callback(seq.substring(idx, emojiEnd))
                idx = emojiEnd
                continue
            }

            val codePoint = seq.codePointAt(idx)

            val oldIdx = idx
            idx = it.following(idx)

            if (!INVISIBLE_UNICODE_CHARS.contains(codePoint)) {
                callback(seq.substring(oldIdx, idx))
            }
        }
    }
}

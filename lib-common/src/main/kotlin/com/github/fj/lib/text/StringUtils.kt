/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import java.util.regex.Pattern

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

/*
 * Referenced from: https://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
 * Pretty display at: https://www.fileformat.info/info/unicode/category/Zs/list.htm
 *
 * Type notation to ensure this collection is immutable even though a reference leakage happens
 */
@Suppress("DuplicatedCode")
private val UNICODE_BLANK_CHARS: Set<Char> = setOf(
    '\u0009', // control-0009
    '\u000A', // control-000A
    '\u000B', // control-000B
    '\u000C', // control-000C
    '\u000D', // control-000D
    '\u001C', // FILE SEPARATOR
    '\u001D', // GROUP SEPARATOR
    '\u001E', // RECORD SEPARATOR
    '\u001F', // UNIT SEPARATOR
    '\u0020', // SPACE
    '\u0085', // control-0085
    '\u00A0', // NO-BREAK SPACE
    '\u1680', // OGHAM SPACE MARK
    '\u2000', // EN QUAD
    '\u2001', // EM QUAD
    '\u2002', // EN SPACE
    '\u2003', // EM SPACE
    '\u2004', // THREE-PER-EM SPACE
    '\u2005', // FOUR-PER-EM SPACE
    '\u2006', // SIX-PER-EM SPACE
    '\u2007', // FIGURE SPACE
    '\u2008', // PUNCTUATION SPACE
    '\u2009', // THIN SPACE
    '\u200A', // HAIR SPACE
    '\u202F', // NARROW NO-BREAK SPACE
    '\u205F', // MEDIUM MATHEMATICAL SPACE
    '\u3000'  // IDEOGRAPHIC SPACE
)

@SuppressWarnings("ReturnCount")
fun String?.isNullOrUnicodeBlank(): Boolean {
    if (isNullOrBlank()) {
        return true
    }

    this.forEach {
        if (!UNICODE_BLANK_CHARS.contains(it)) {
            return false
        }
    }

    return true
}

fun String.matchesIn(pattern: Pattern) = pattern.matcher(this).matches()

/**
 * Converts this String to boolean. This function will yield correct results
 * only on a String of one length. Expected results are described below:
 *
 * ------------------------------------------------------------------------
 *  Input String                                      | Result
 * ------------------------------------------------------------------------
 *  'N' or 'n'                                        | false
 *  'Y' or 'y'                                        | true
 *  Any string with > 2 letters                       | true
 *  Empty string                                      | false
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Mar - 2018
 */
fun String.ynToBoolean(): Boolean {
    return when (this.length) {
        0 -> false
        1 -> toLowerCase() != "n"
        else -> true
    }
}

fun Boolean.toYn(): String {
    return if (this) {
        "Y"
    } else {
        "N"
    }
}

@SuppressWarnings("MagicNumber")
fun ByteArray.toHexString(): String {
    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v = this[j].toInt() and 0xFF
        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}

/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import java.util.*
import java.util.regex.Pattern

/*
 * Referenced from: https://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
 * Pretty display at: https://www.fileformat.info/info/unicode/category/Zs/list.htm
 *
 * Type notation to ensure this collection is immutable even though a reference leakage happens
 */
private val UNICODE_BLANK_CHARS: Set<Char> = HashSet<Char>().apply {
    add('\u0009') // control-0009
    add('\u000A') // control-000A
    add('\u000B') // control-000B
    add('\u000C') // control-000C
    add('\u000D') // control-000D
    add('\u001C') // FILE SEPARATOR
    add('\u001D') // GROUP SEPARATOR
    add('\u001E') // RECORD SEPARATOR
    add('\u001F') // UNIT SEPARATOR
    add('\u0020') // SPACE
    add('\u0085') // control-0085
    add('\u00A0') // NO-BREAK SPACE
    add('\u1680') // OGHAM SPACE MARK
    add('\u2000') // EN QUAD
    add('\u2001') // EM QUAD
    add('\u2002') // EN SPACE
    add('\u2003') // EM SPACE
    add('\u2004') // THREE-PER-EM SPACE
    add('\u2005') // FOUR-PER-EM SPACE
    add('\u2006') // SIX-PER-EM SPACE
    add('\u2007') // FIGURE SPACE
    add('\u2008') // PUNCTUATION SPACE
    add('\u2009') // THIN SPACE
    add('\u200A') // HAIR SPACE
    add('\u202F') // NARROW NO-BREAK SPACE
    add('\u205F') // MEDIUM MATHEMATICAL SPACE
    add('\u3000') // IDEOGRAPHIC SPACE
}

fun String?.isNullOrUnicodeBlank(): Boolean {
    if (isNullOrBlank()) {
        return true
    }

    this!!.forEach {
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

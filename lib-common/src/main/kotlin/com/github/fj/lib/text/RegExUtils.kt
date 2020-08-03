/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.text

import java.util.*

const val REGEX_HEXADECIMAL = "[0-9a-fA-F]"

/**
 * Matches all UUID patterns through version 1 to 5. Note that JVM [UUID] implementation uses UUID v4 by default.
 */
const val REGEX_UUID = "$REGEX_HEXADECIMAL{8}-" +
        "$REGEX_HEXADECIMAL{4}-" +
        "[1-5]$REGEX_HEXADECIMAL{3}-" +
        "[89AaBb]$REGEX_HEXADECIMAL{3}-" +
        "$REGEX_HEXADECIMAL{12}"

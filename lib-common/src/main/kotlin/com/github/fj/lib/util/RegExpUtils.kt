/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
import java.util.regex.Pattern

const val REGEXP_HEXADECIMAL = "(?i)[0-9A-F]"

val PATTERN_HEXADECIMAL = Pattern.compile(REGEXP_HEXADECIMAL)

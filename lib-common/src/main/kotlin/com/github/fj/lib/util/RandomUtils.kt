/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util

import java.security.SecureRandom
import java.util.*
import java.util.concurrent.ThreadLocalRandom

private const val RANDOM_ALPHANUMERIC_CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890"
private const val RANDOM_CAPITAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZz1234567890"

fun getRandomAlphaNumericString(length: Int) =
    getRandomAlphaNumericStringInternal(length, RANDOM_ALPHANUMERIC_CHARS)

fun getRandomCapitalAlphaNumericString(length: Int) =
    getRandomAlphaNumericStringInternal(length, RANDOM_CAPITAL_CHARS)

private fun getRandomAlphaNumericStringInternal(length: Int, pool: CharSequence): String {
    val random = ThreadLocalRandom.current()
    val sb = StringBuffer(length)
    for (loop in 0 until length) {
        val index = random.nextInt(pool.length)
        sb.append(pool[index])
    }

    return sb.toString()
}

fun getRandomBytes(length: Int) = ByteArray(length).apply {
    Random().nextBytes(this)
}

fun getSecureRandomBytes(length: Int) = ByteArray(length).apply {
    SecureRandom().nextBytes(this)
}

fun getRandomPositiveInt(upperBound: Int = Integer.MAX_VALUE) =
    ThreadLocalRandom.current().nextInt(upperBound)

fun getRandomPositiveLong(upperBound: Long = Long.MAX_VALUE) =
    ThreadLocalRandom.current().nextLong(upperBound)

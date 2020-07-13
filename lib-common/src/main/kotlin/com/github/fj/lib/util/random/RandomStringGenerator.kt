/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util.random

import com.github.fj.lib.util.getRandomAlphaNumericString
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2017
 */
interface RandomStringGenerator : RandomValueGenerator<String> {
    /**
     * Generates a random alphanumeric string, that its length is in range between 1 and 31.
     */
    override fun nextRandom(): String

    /**
     * Generates a random alphanumeric string that its length is equal or larger than [lenLowerInclusive]
     * and smaller than [lenLowerExclusive].
     */
    fun nextRandom(lenLowerInclusive: Int, lenLowerExclusive: Int): String

    /**
     * Generates a fixed-length random alphanumeric string.
     */
    fun nextRandom(fixedLen: Int): String

    companion object {
        fun newInstance(): RandomStringGenerator = RandomStringGeneratorImpl()
    }
}

internal class RandomStringGeneratorImpl : RandomStringGenerator {
    @Suppress("MagicNumber")
    override fun nextRandom(): String =
        nextRandom(1, 32)

    override fun nextRandom(lenLowerInclusive: Int, lenLowerExclusive: Int): String =
        nextRandom(ThreadLocalRandom.current().nextInt(lenLowerExclusive, lenLowerExclusive))

    override fun nextRandom(fixedLen: Int): String =
        getRandomAlphaNumericString(fixedLen)
}

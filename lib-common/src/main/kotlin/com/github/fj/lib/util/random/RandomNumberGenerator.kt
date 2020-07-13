/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util.random

import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2017
 */
interface RandomNumberGenerator<T : Number> : RandomValueGenerator<T> {
    /**
     * Generates a random number object, that its value is in range between smallest to largest.
     * For example, This method of an instance of `RandomNumberGenerator<Int>` should produce a number
     * in range between [Int.MIN_VALUE] to [Int.MAX_VALUE].
     */
    override fun nextRandom(): T

    /**
     * Generates a random number object, that its value is in range between [lowerInclusive] to [higherExclusive].
     * However as the parameter name implies, the minimum possible value must be equal or larger than [lowerInclusive]
     * and the maximum possible value must be smaller than [higherExclusive].
     */
    fun nextRandom(lowerInclusive: T, higherExclusive: T): T

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : Number> newInstance(klass: KClass<T>): RandomNumberGenerator<T> = when (klass) {
            Int::class    -> RandomIntGenerator()
            Long::class   -> RandomLongGenerator()
            Double::class -> RandomDoubleGenerator()
            else          -> {
                val errMsg = if (klass.isSubclassOf(Number::class)) {
                    "$klass is not a subtype of Number."
                } else {
                    "Random value generation for $klass is not supported."
                }

                throw IllegalArgumentException(errMsg)
            }
        } as RandomNumberGenerator<T>
    }
}

internal class RandomIntGenerator : RandomNumberGenerator<Int> {
    override fun nextRandom(): Int = ThreadLocalRandom.current().nextInt()

    override fun nextRandom(lowerInclusive: Int, higherExclusive: Int): Int =
        ThreadLocalRandom.current().nextInt(lowerInclusive, higherExclusive)
}

internal class RandomLongGenerator : RandomNumberGenerator<Long> {
    override fun nextRandom(): Long = ThreadLocalRandom.current().nextLong()

    override fun nextRandom(lowerInclusive: Long, higherExclusive: Long): Long =
        ThreadLocalRandom.current().nextLong(lowerInclusive, higherExclusive)
}

internal class RandomDoubleGenerator : RandomNumberGenerator<Double> {
    override fun nextRandom(): Double = ThreadLocalRandom.current().nextDouble()

    override fun nextRandom(lowerInclusive: Double, higherExclusive: Double): Double =
        ThreadLocalRandom.current().nextDouble(lowerInclusive, higherExclusive)
}

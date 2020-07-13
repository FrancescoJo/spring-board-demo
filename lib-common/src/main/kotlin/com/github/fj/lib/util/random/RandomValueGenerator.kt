/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util.random

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2017
 */
interface RandomValueGenerator<T> {
    /**
     * Generates next random value <T>. Strategies of choosing an random seed, result derivation of this value
     * is depends on individual implementations.
     */
    fun nextRandom(): T
}

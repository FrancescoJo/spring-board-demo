/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2019
 */
fun ClosedRange<Int>.toArray() = Array((endInclusive - start) + 1) { i -> start + i }

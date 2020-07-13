/*
 * FJ's Kotlin utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.lang

import java.math.BigDecimal
import kotlin.math.round

/**
 * 'Almost' precise double rounding-up function in given decimal point.
 *
 * As this method is stated as 'almose precise', do not use this for places where precision is critical -
 * i.e.) Money calculation. Use [BigDecimal] instead for those cases.
 *
 * https://discuss.kotlinlang.org/t/how-do-you-round-a-number-to-n-decimal-places/8843
 */
fun Double.roundToDecimal(decimals: Int): Double {
    var multiplier = 1.0
    @Suppress("MagicNumber")
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.io

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToLong

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
@Suppress("MagicNumber")
enum class FileSizeUnit constructor(
    val min: Long,
    val max: Long,
    val identifier: String
) {
    BYTES(0L, 1024L, "B"),
    KIBIBYTES(1024L, 1024L * 1024L, "KiB"),
    MEBIBYTES(1024L * 1024L, 1024L * 1024L * 1024L, "MiB"),
    GIBIBYTES(1024L * 1024L * 1024L, 1024L * 1024L * 1024L * 1024L, "GiB"),
    TEBIBYTES(1024L * 1024L * 1024L * 1024L, 1024L * 1024L * 1024L * 1024L * 1024L, "TiB");

    fun isInRange(size: Long): Boolean {
        return size in (min until max)
    }

    fun asString(size: Long, locale: Locale): String {
        val df = (NumberFormat.getNumberInstance(locale) as DecimalFormat).apply {
            applyLocalizedPattern("#,###,###.##")
        }
        val sizeDouble = if (0L == min) {
            size.toDouble()
        } else {
            size.toDouble() / min
        }

        return df.format(sizeDouble) + " " + identifier
    }

    fun asBytes(value: Double): Long {
        return if (this == BYTES) {
            value.roundToLong()
        } else (value * min).roundToLong()
    }

    companion object {
        fun getBest(size: Long): FileSizeUnit {
            val units = values()
            for (i in units.indices.reversed()) {
                val unit = units[i]
                if (unit.isInRange(size)) {
                    return unit
                }
            }
            return BYTES
        }
    }
}

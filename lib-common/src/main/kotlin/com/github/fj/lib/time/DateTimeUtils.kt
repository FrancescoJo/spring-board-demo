/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
@file:Suppress("TooManyFunctions")

package com.github.fj.lib.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Any database compliant minimum LocalDateTime
 * Use this value if [java.time.LocalDateTime.MIN] underflows in your DBMS.
 */
@Suppress("MagicNumber")
val LOCAL_DATE_TIME_MIN: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0, 0)

/**
 * Any database compliant minimum LocalDateTime
 * Use this value if [java.time.LocalDateTime.MAX] overflows in your DBMS.
 */
@Suppress("MagicNumber")
val LOCAL_DATE_TIME_MAX: LocalDateTime = LocalDateTime.of(9999, Month.DECEMBER, 31, 23, 59, 59, 0)

fun utcNow(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

fun LocalDateTime.utcEpochSecond(): Long = this.toEpochSecond(ZoneOffset.UTC)

/**
 * Calculates hour differences between UTC and current system time Zone.
 */
fun getHourDiffToUtc(): Int = ZoneOffset.systemDefault().getHourDiff()

/**
 * Calculates hour differences between UTC and given Zone Id.
 */
fun ZoneId.getHourDiff(): Int = parseZoneOffset(rules.getOffset(utcNow()))[0]

/**
 * Calculates minute differences between UTC and current system time Zone.
 */
fun getMinuteDiffToUtc(): Int = ZoneOffset.systemDefault().getMinuteDiff()

/**
 * Calculates minute differences between UTC and given Zone Id.
 */
fun ZoneId.getMinuteDiff(): Int = parseZoneOffset(rules.getOffset(utcNow()))[1]

/**
 * Calculates minute differences between UTC and current system time Zone.
 */
fun getSecondDiffToUtc(): Int = ZoneOffset.systemDefault().getSecondDiff()

/**
 * Calculates minute differences between UTC and given Zone Id.
 */
fun ZoneId.getSecondDiff(): Int = parseZoneOffset(rules.getOffset(utcNow()))[2]

// This logic is working based on java.time.ZoneOffset#buildId(int) implementation.
private fun parseZoneOffset(zoneOffset: ZoneOffset): IntArray {
    @Suppress("MagicNumber")
    val diffSizes = 3

    return with(zoneOffset.toString().split(":")) {
        if (isEmpty()) {
            return@with IntArray(diffSizes) { 0 }
        }

        val sign = if (get(0).startsWith("-")) {
            -1
        } else {
            1
        }

        val hourDiff = get(0).let {
            val hourStr = if (it.startsWith("+") || it.startsWith("-")) {
                it.substring(1)
            } else {
                it.substring(0)
            }

            sign * hourStr.toInt()
        }

        val minuteDiff = sign * get(1).toInt()

        val secondDiff = if (size > 2) {
            sign * get(2).toInt()
        } else {
            0
        }

        return@with IntArray(diffSizes).apply {
            set(0, hourDiff)
            set(1, minuteDiff)
            set(2, secondDiff)
        }
    }
}

/**
 * Do not use this method after year 2038 if timestamp is 4 bytes long only.
 */
fun utcLocalDateTimeOf(timestamp: Number): LocalDateTime =
    utcLocalDateTimeOf(timestamp.toLong())

fun utcLocalDateTimeOf(timestamp: Long): LocalDateTime = if (timestamp > Integer.MAX_VALUE) {
    LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC)
} else {
    LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC)
}

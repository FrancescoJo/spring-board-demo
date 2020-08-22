/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.util

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Jul - 2020
 */
import java.nio.ByteBuffer
import java.util.*

private val EMPTY_UUID = UUID(0, 0)

fun UUID.toByteArray(): ByteArray {
    @Suppress("MagicNumber")
    val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(mostSignificantBits)
    bb.putLong(leastSignificantBits)
    return bb.array()
}

fun ByteArray.toUUID(): UUID {
    @Suppress("MagicNumber")
    if (this.size != 16) {
        throw IllegalArgumentException("Only 16-bytes long byte array could be cast to UUID.")
    }

    val byteBuffer: ByteBuffer = ByteBuffer.wrap(this)
    val high: Long = byteBuffer.long
    val low: Long = byteBuffer.long
    return UUID(high, low)
}

object UuidExtensions {
    val EMPTY_UUID: UUID = com.github.fj.lib.util.EMPTY_UUID
}

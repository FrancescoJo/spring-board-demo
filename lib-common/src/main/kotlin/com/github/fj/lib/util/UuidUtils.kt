/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
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
    val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(mostSignificantBits)
    bb.putLong(leastSignificantBits)
    return bb.array()
}

@Suppress("MagicNumber")
fun ByteArray.toUUID(): UUID {
    if (this.size != 16) {
        throw IllegalArgumentException("Only 16-bytes long byte array could be cast to UUID.")
    }

    val byteBuffer: ByteBuffer = ByteBuffer.wrap(this)
    val high: Long = byteBuffer.long
    val low: Long = byteBuffer.long
    return UUID(high, low)
}

class UuidExtensions {
    companion object {
        val EMPTY_UUID: UUID = com.github.fj.lib.util.EMPTY_UUID
    }
}

/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.net

import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

private val EMPTY_INET_ADDRESS = InetAddress.getByName("0.0.0.0")
private val LOCALHOST = InetAddress.getByName("localhost")

/**
 * IPv4 to IPv6 rule is from
 * [RFC 4291](https://tools.ietf.org/html/rfc4291#section-2.5.5.2).
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2018
 */
fun InetAddress.toIpV6AddressBytes(): ByteArray {
    @Suppress("MagicNumber")
    return when (this) {
        is Inet4Address -> byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0xFF.toByte(), 0xFF.toByte(),
            address[0], address[1], address[2], address[3]
        )
        is Inet6Address -> this.address
        else -> throw UnsupportedOperationException(
            "Not an IP compatible address: $hostAddress")
    }
}

/**
 * Converts given [kotlin.ByteArray] which may represents IPv4 or IPv6 address,
 * to [java.net.InetAddress] representation.
 *
 * The operating [kotlin.ByteArray] size must be either 4 or 16.
 */
fun ByteArray.toInetAddress(): InetAddress {
    @Suppress("MagicNumber")
    return if (size != 4 && size != 16) {
        EMPTY_INET_ADDRESS
    } else {
        InetAddress.getByAddress(this)
    }
}

/**
 * Determines that operating [java.net.InetAddress] represents whether an IPv4 address or not.
 */
fun InetAddress.isIpV4Address(): Boolean {
    return this is Inet4Address
}

object InetAddressExtensions {
    /**
     * Workaround for
     *
     * ```
     * fun InetAddress.Companion.empty(): InetAddress = EMPTY_INET_ADDRESS
     * ```
     *
     * which is currently(Kotlin 1.0) impossible.
     * Read [Kotlin Youtrack issues](https://youtrack.jetbrains.com/issue/KT-11968) for more information.
     */
    val EMPTY_INET_ADDRESS: InetAddress = com.github.fj.lib.net.EMPTY_INET_ADDRESS

    val LOCALHOST: InetAddress = com.github.fj.lib.net.LOCALHOST
}

fun InetAddress?.isNullOrEmpty(): Boolean = this == null || this == EMPTY_INET_ADDRESS

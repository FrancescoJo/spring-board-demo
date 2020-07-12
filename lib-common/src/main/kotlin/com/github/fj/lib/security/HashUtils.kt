/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.security

import java.security.MessageDigest

fun String.toSha256Bytes(): ByteArray = MessageDigest.getInstance("SHA-256").run {
    digest(this@toSha256Bytes.toByteArray())
}

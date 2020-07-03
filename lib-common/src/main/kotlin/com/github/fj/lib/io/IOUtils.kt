/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.io

import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2018
 */
fun InputStream.dumpAsString(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

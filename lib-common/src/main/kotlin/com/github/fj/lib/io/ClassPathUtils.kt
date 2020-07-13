/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.io

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jun - 2018
 */
import java.io.IOException
import java.io.InputStream
import java.net.URL

/**
 * Searches given resource with `resName` which is located at default package.
 *
 * @param resName Resource name, usually a file name
 * @return File in `resources` directory as [InputStream] representation.
 * @throws IOException If given resource with `resName` is not found.
 */
@Throws(IOException::class)
fun Any.getClassPathResource(resName: String): InputStream =
    getClassPathResourceStream(this::class.java.`package`.name, resName)

/**
 * Searches given resource with `resName` which is located at given `pkgName`.
 *
 * @param pkgName Package name, where the resource is located at
 * @param resName Resource name, usually a file name
 * @return File in `resources` directory as [InputStream] representation.
 * @throws IOException If given resource with `resName` is not found.
 */
@Throws(IOException::class)
fun Any.getClassPathResourceStream(pkgName: String, resName: String): InputStream =
    getClassPathResourceFile(pkgName, resName).openStream()

/**
 * Searches given resource with `resName` which is located at given `pkgName`.
 *
 * @param pkgName Package name, where the resource is located at
 * @param resName Resource name, usually a file name
 * @return File in `resources` directory as [InputStream] representation.
 * @throws IOException If given resource with `resName` is not found.
 */
@Throws(IOException::class)
fun Any.getClassPathResourceFile(pkgName: String, resName: String): URL {
    val pathPrefix = if (pkgName.isEmpty()) {
        ""
    } else {
        pkgName.replace("[.]".toRegex(), "/") + "/"
    }

    val resPath = pathPrefix + resName
    if (resPath.isEmpty()) {
        println("Resource path is empty")
    }

    return javaClass.classLoader.getResource(resPath)
        ?: throw IllegalArgumentException("Resource with name $resPath is not found!!")
}

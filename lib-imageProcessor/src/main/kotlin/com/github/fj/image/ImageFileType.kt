/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.image

import com.drew.imaging.FileType

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2017
 */
enum class ImageFileType(
    val longName: String,
    val mimeType: String,
    val extensions: List<String>
) {
    JPEG("Joint Photographic Experts Group", "image/jpeg", listOf("jpg", "jpeg", "jpe")),
    PNG("Portable Network Graphics", "image/png", listOf("png")),
    RIFF("Resource Interchange File Format", "", emptyList()),
    WEBP("WEBP", "image/webp", listOf("webp"));

    companion object {
        internal fun from(origType: FileType): ImageFileType = when (origType) {
            FileType.Jpeg -> JPEG
            FileType.Png  -> PNG
            FileType.Riff -> RIFF
            FileType.WebP -> WEBP
            else          -> throw IllegalArgumentException("Unsupported file type: $origType")
        }
    }
}

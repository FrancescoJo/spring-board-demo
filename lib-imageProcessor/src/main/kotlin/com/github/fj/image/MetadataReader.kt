/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.image

import com.drew.imaging.FileType
import com.drew.imaging.FileTypeDetector
import com.drew.imaging.ImageProcessingException
import com.drew.imaging.jpeg.JpegMetadataReader
import com.drew.imaging.png.PngMetadataReader
import com.drew.lang.StreamReader
import com.drew.metadata.Metadata
import com.drew.metadata.MetadataException
import com.drew.metadata.jpeg.JpegDirectory
import com.drew.metadata.png.PngDirectory
import com.drew.metadata.webp.WebpDirectory
import com.drew.metadata.webp.WebpRiffHandler
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Reads [Metadata] from a [File] object.
 *
 * @return a populated [Metadata] object containing directories of tags with values and any processing errors.
 * @throws ImageProcessingException for general processing errors.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2017
 */
@Throws(IOException::class, ImageProcessingException::class, MetadataException::class)
fun File.readImageMetadata(): ImageMetadata {
    FileInputStream(this).use { inputStream ->
        val bufferedInputStream = BufferedInputStream(inputStream)

        @Suppress("MagicNumber")
        return when (val fileType = FileTypeDetector.detectFileType(bufferedInputStream)) {
            FileType.Jpeg -> {
                val metadata = JpegMetadataReader.readMetadata(bufferedInputStream)
                val jpeg = metadata.getFirstDirectoryOfType(JpegDirectory::class.java)

                ImageMetadata.create(
                    type = ImageFileType.from(fileType),
                    width = jpeg.imageWidth,
                    height = jpeg.imageHeight,
                    frameCount = 1,
                    colourDepth = 3,
                    hasAlpha = false
                )
            }
            FileType.Png -> {
                val metadata = PngMetadataReader.readMetadata(bufferedInputStream)
                val png = metadata.getFirstDirectoryOfType(PngDirectory::class.java)

                ImageMetadata.create(
                    type = ImageFileType.from(fileType),
                    width = png.getInt(PngDirectory.TAG_IMAGE_WIDTH),
                    height = png.getInt(PngDirectory.TAG_IMAGE_HEIGHT),
                    frameCount = 1,
                    colourDepth = when (png.getInt(PngDirectory.TAG_COLOR_TYPE)) {
                        0    -> 1 /* grayscale */
                        6    -> 4 /* argb */
                        else -> 3 /* naïve assumption */
                    },
                    hasAlpha = true
                )
            }
            FileType.Riff,
            FileType.WebP -> {
                val metadata = Metadata()
                val numFrames = RiffReader().processRiff(
                    StreamReader(bufferedInputStream), WebpRiffHandler(metadata)
                )
                val webP = metadata.getFirstDirectoryOfType(WebpDirectory::class.java)
                val isAnimation = webP.containsTag(WebpDirectory.TAG_IS_ANIMATION)

                ImageMetadata.create(
                    type = ImageFileType.from(fileType),
                    width = webP.getInt(WebpDirectory.TAG_IMAGE_WIDTH),
                    height = webP.getInt(WebpDirectory.TAG_IMAGE_HEIGHT),
                    frameCount = if (isAnimation) numFrames else 1,
                    colourDepth = if (webP.containsTag(WebpDirectory.TAG_HAS_ALPHA)) {
                        4
                    } else {
                        3
                    },
                    hasAlpha = webP.containsTag(WebpDirectory.TAG_HAS_ALPHA)
                )
            }
            else -> throw UnsupportedOperationException("Currently only JPEG, PNG, WEBP formats are supported.")
        }
    }
}

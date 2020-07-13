/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.image

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2017
 */
interface ImageMetadata {
    val type: ImageFileType

    val width: Int

    val height: Int

    /**
     * 1 for still image, any number for animating images (APNG, WEBP)
     */
    val frameCount: Int

    /**
     * Number of bytes of colour depth. 1: grayscale, 3: RGB, 4: ARGB.
     */
    val colourDepth: Int

    val hasAlpha: Boolean

    /**
     * This value is derived by assumption that every images are true coloured.
     * In other words, this value always represents the worst case(of memory consumption).
     */
    val inMemSize: Long
        get() = (width * height * colourDepth * frameCount).toLong()

    companion object {
        private data class ImageMetadataImpl(
            override val type: ImageFileType,
            override val width: Int,
            override val height: Int,
            override val frameCount: Int,
            override val colourDepth: Int,
            override val hasAlpha: Boolean
        ) : ImageMetadata

        @Suppress("LongParameterList")
        internal fun create(
            type: ImageFileType,
            width: Int,
            height: Int,
            frameCount: Int,
            colourDepth: Int,
            hasAlpha: Boolean
        ): ImageMetadata = ImageMetadataImpl(
            type = type,
            width = width,
            height = height,
            frameCount = frameCount,
            colourDepth = colourDepth,
            hasAlpha = hasAlpha
        )
    }
}

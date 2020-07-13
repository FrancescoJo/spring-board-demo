/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase

import com.github.fj.image.ImageFileType
import com.github.fj.image.ImageMetadata
import com.github.fj.image.readImageMetadata
import com.github.fj.lib.io.getClassPathResourceFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2017
 */
class MetadataReaderTest {
    @ParameterizedTest
    @MethodSource("testImageFiles")
    fun `readImageMetadata meets expectations on various files`(imgFileName: String, expected: ImageMetadata) {
        // given:
        val imgFile = File(getClassPathResourceFile("", imgFileName).file)

        // when:
        val actual = imgFile.readImageMetadata()

        // then:
        assertThat(actual, `is`(expected))
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testImageFiles(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "jpg-550x368.jpg", ImageMetadata.create(
                        type = ImageFileType.JPEG,
                        width = 550,
                        height = 368,
                        frameCount = 1,
                        colourDepth = 3,
                        hasAlpha = false
                    )
                ),
                Arguments.of(
                    "png-768x512-grayscale.png", ImageMetadata.create(
                        type = ImageFileType.PNG,
                        width = 768,
                        height = 512,
                        frameCount = 1,
                        colourDepth = 1,
                        hasAlpha = true
                    )
                ),
                Arguments.of(
                    "png-3400x1700-argb.png", ImageMetadata.create(
                        type = ImageFileType.PNG,
                        width = 3400,
                        height = 1700,
                        frameCount = 1,
                        colourDepth = 4,
                        hasAlpha = true
                    )
                ),
                Arguments.of(
                    "webp-550x368-still.webp", ImageMetadata.create(
                        type = ImageFileType.WEBP,
                        width = 550,
                        height = 368,
                        frameCount = 1,
                        colourDepth = 3,
                        hasAlpha = false
                    )
                ),
                Arguments.of(
                    "webp-400x400-animated.webp", ImageMetadata.create(
                        type = ImageFileType.WEBP,
                        width = 400,
                        height = 400,
                        frameCount = 12,
                        colourDepth = 4,
                        hasAlpha = true
                    )
                )
            )
        }
    }
}

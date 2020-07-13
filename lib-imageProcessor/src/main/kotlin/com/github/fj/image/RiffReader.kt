/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.image

import com.drew.imaging.riff.RiffHandler
import com.drew.imaging.riff.RiffProcessingException
import com.drew.lang.SequentialReader
import com.drew.lang.annotations.NotNull
import java.io.IOException

/**
 * Processes RIFF-formatted data, calling into client code via that [RiffHandler] interface.
 *
 * For information on this file format, see:
 *
 *  * http://en.wikipedia.org/wiki/Resource_Interchange_File_Format
 *  * https://developers.google.com/speed/webp/docs/riff_container
 *  * https://www.daubnet.com/en/file-format-riff
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Oct - 2017
 */
@Suppress("MagicNumber")
internal class RiffReader {
    /**
     * Processes a RIFF data sequence.
     *
     * @param reader  the [SequentialReader] from which the data should be read
     * @param handler the [RiffHandler] that will coordinate processing and accept read values
     * @throws RiffProcessingException if an error occurred during the processing of RIFF data that could not be
     * ignored or recovered from
     * @throws IOException             an error occurred while accessing the required data
     */
    @Throws(RiffProcessingException::class, IOException::class)
    fun processRiff(@NotNull reader: SequentialReader, @NotNull handler: RiffHandler): Int {
        // RIFF files are always little-endian
        reader.isMotorolaByteOrder = false

        // PROCESS FILE HEADER

        val fileFourCC = reader.getString(4)

        if (fileFourCC != "RIFF") throw RiffProcessingException("Invalid RIFF header: $fileFourCC")

        // The total size of the chunks that follow plus 4 bytes for the 'WEBP' FourCC
        var sizeLeft = reader.int32

        val identifier = reader.getString(4)
        sizeLeft -= 4

        if (!handler.shouldAcceptRiffIdentifier(identifier))
            return 0

        // PROCESS CHUNKS
        var numFrames = 0
        while (sizeLeft != 0) {
            val chunkFourCC = reader.getString(4)
            val chunkSize = reader.int32
            sizeLeft -= 8

            // NOTE we fail a negative chunk size here (greater than 0x7FFFFFFF) as Java cannot
            // allocate arrays larger than this.
            if (chunkSize < 0 || sizeLeft < chunkSize)
                throw RiffProcessingException("Invalid RIFF chunk size")

            if ("ANMF" == chunkFourCC) numFrames++

            if (handler.shouldAcceptChunk(chunkFourCC)) {
                handler.processChunk(chunkFourCC, reader.getBytes(chunkSize))
            } else {
                reader.skip(chunkSize.toLong())
            }

            sizeLeft -= chunkSize

            // Skip any padding byte added to keep chunks aligned to even numbers of bytes
            if (chunkSize % 2 == 1) {
                reader.int8
                sizeLeft--
            }
        }

        return numFrames
    }
}

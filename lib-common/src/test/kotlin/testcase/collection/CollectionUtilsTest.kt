/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.collection

import com.github.fj.lib.collection.findInsertPosition
import com.github.fj.lib.collection.iterationsOf
import com.github.fj.lib.collection.resize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2018
 */
class CollectionUtilsTest {
    @Test
    fun `resize yields correct result upon given data set`() {
        assertEquals(listOf(0)         , listOf(0, 1, 2).resize(1))
        assertEquals(listOf(0, 1)      , listOf(0, 1).resize(2))
        assertEquals(listOf(0, 1, 9, 9), listOf(0, 1).resize(4) { 9 })

        assertThrows<IllegalArgumentException> { listOf(0, 1).resize(4, null) }
    }

    @Test
    fun `findInsertPosition yields correct result upon given sorted list`() {
        val values = listOf(1, 3, 4, 5, 6, 8, 9)
        val withPosition: (Int) -> Int = { values.findInsertPosition(it) }

        assertEquals(0, withPosition(0))
        assertEquals(1, withPosition(2))
        assertEquals(4, withPosition(5))
        assertEquals(7, withPosition(10))
    }

    @Test
    fun `Int#iterations test`() {
        val strings = 3.iterationsOf { "a" }

        assertEquals(strings, listOf("a", "a", "a"))
    }
}

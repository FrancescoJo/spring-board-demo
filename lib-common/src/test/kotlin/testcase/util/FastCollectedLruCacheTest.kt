/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.util

import com.github.fj.lib.util.FastCollectedLruCacheImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2018
 */
class FastCollectedLruCacheImplTest {
    private lateinit var sut: FastCollectedLruCacheImpl<String, Any>

    @BeforeEach
    fun setup() {
        this.sut = FastCollectedLruCacheImpl(CACHE_SIZE)
    }

    @Test
    fun `consecutive put eliminates eldest entries`() {
        // given:
        val expectedSoftCacheSize = CACHE_SIZE - (CACHE_SIZE * 0.75f).roundToInt()
        for (i in 1..CACHE_SIZE) {
            sut.put("Num_$i", i)
        }

        // then:
        assertEquals((CACHE_SIZE * 0.75f).roundToInt(), sut.hardCache.size)
        assertNull(sut.hardCache["Num_1"])
        assertNull(sut.hardCache["Num_2"])
        assertEquals(expectedSoftCacheSize, sut.softCache.size)
    }

    @Test
    fun `get causes softref cache entry moved into hard cache`() {
        // given:
        for (i in 1..CACHE_SIZE) {
            sut.put("Num_$i", i)
        }

        // when:
        sut.get("Num_1")

        // then:
        assertNull(sut.softCache["Num_1"])
        assertTrue(sut.hardCache.containsKey("Num_1"))
    }

    companion object {
        private const val CACHE_SIZE = 10
    }
}

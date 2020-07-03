/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package testcase.net

import com.github.fj.lib.net.InetAddressExtensions
import com.github.fj.lib.net.isNullOrEmpty
import com.github.fj.lib.net.toInetAddress
import com.github.fj.lib.net.toIpV6AddressBytes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.net.InetAddress

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Mar - 2018
 */
class InetAddressUtilsTest {
    @ParameterizedTest(name = "InetAddress conversion for ''{0}''")
    @ValueSource(strings = ["localhost", "8.8.8.8"])
    fun `InetAddress to bytes conversion and vice versa should be exactly same`(hostname: String) {
        val sourceAddress = InetAddress.getByName(hostname)
        val bytes = sourceAddress.toIpV6AddressBytes()

        assertEquals(sourceAddress, bytes.toInetAddress())
    }

    @Test
    fun `InetAddress#isEmpty works properly on InetAddress#empty result`() {
        val emptyInetAddress = InetAddressExtensions.EMPTY_INET_ADDRESS

        assertTrue(emptyInetAddress.isNullOrEmpty())
    }
}

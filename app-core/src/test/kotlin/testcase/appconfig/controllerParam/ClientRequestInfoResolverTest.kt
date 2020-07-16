/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.appconfig.controllerParam

import com.github.fj.board.appconfig.controllerParam.ClientRequestInfoResolver
import com.github.fj.board.component.auth.AuthenticationObjectImpl
import com.github.fj.board.exception.client.UnauthorisedException
import com.github.fj.board.exception.server.NotImplementedException
import com.github.fj.board.vo.auth.ClientRequestInfo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.springframework.core.MethodParameter
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.NativeWebRequest
import test.com.github.fj.board.component.auth.JwtObjectBuilder
import test.com.github.fj.board.component.security.HttpAuthorizationTokenBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
class ClientRequestInfoResolverTest {
    private lateinit var sut: ClientRequestInfoResolver

    @BeforeEach
    fun setup() {
        this.sut = ClientRequestInfoResolver()
    }

    @Test
    fun `fail if no authentication object is found in SecurityContext`() {
        // given:
        val webReq = mock(NativeWebRequest::class.java)

        // expect:
        assertThrows<UnauthorisedException> {
            testResolveArgument(webReq)
        }
    }

    @Test
    fun `fail if Authentication object is not compatible to AuthenticationObjectImpl`() {
        // given:
        val webReq = mock(NativeWebRequest::class.java)
        val authObject = mock(Authentication::class.java)

        // when:
        SecurityContextHolder.getContext().authentication = authObject

        // then:
        assertThrows<NotImplementedException> {
            testResolveArgument(webReq)
        }
    }

    @Test
    fun `fail if NativeWebRequest object is not compatible to HttpServletRequest`() {
        // given:
        val webReq = mock(NativeWebRequest::class.java)
        val authObject = AuthenticationObjectImpl(
            JwtObjectBuilder.createRandom(), HttpAuthorizationTokenBuilder.createRandom()
        )

        // when:
        SecurityContextHolder.getContext().authentication = authObject

        // then:
        assertThrows<NotImplementedException> {
            testResolveArgument(webReq)
        }
    }

    // success

    @AfterEach
    fun teardown() {
        SecurityContextHolder.getContext().authentication = null
    }

    private fun testResolveArgument(webReq: NativeWebRequest): ClientRequestInfo {
        val param = mock(MethodParameter::class.java)

        return sut.resolveArgument(param, null, webReq, null) as ClientRequestInfo
    }
}

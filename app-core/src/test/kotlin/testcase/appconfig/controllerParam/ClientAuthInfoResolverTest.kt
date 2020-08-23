/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.appconfig.controllerParam

import com.github.fj.board.appconfig.controllerParam.ClientAuthInfoResolver
import com.github.fj.board.component.auth.impl.AuthenticationObjectImpl
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.server.NotImplementedException
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.auth.UserAgent
import com.github.fj.lib.util.getRandomAlphaNumericString
import de.skuzzle.semantic.Version
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.core.MethodParameter
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.context.request.NativeWebRequest
import test.com.github.fj.board.component.auth.JwtObjectBuilder
import test.com.github.fj.board.component.security.HttpAuthorizationTokenBuilder
import test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest
import test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
class ClientAuthInfoResolverTest {
    private lateinit var sut: ClientAuthInfoResolver

    @BeforeEach
    fun setup() {
        this.sut = ClientAuthInfoResolver(emptyList())
    }

    @Test
    fun `fail if authentication is required but no authentication object is found in SecurityContext`() {
        // given:
        val webReq = mock(NativeWebRequest::class.java)
        val mockHttpReq = mockLocalhostServletRequest()

        // when:
        `when`(webReq.nativeRequest).thenReturn(mockHttpReq)

        // then:
        assertThrows<UnauthenticatedException> {
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

    @Test
    fun `success if all conditions are good`() {
        // given:
        val (webReq, authObject, userAgent) = setupSuccessArgs()

        // and:
        val result = testResolveArgument(webReq)!!

        // expect:
        assertThat(result.loginName, `is`(authObject.name))
        assertThat(result.platformType, `is`(userAgent.platformType))
        assertThat(result.platformVer, `is`(userAgent.platformVer))
        assertThat(result.appVer, `is`(userAgent.appVer))
    }

    @Test
    fun `no exceptions but returns null if request matches in authCheckBypassUris`() {
        // given:
        this.sut = ClientAuthInfoResolver(listOf(AntPathRequestMatcher("/**")))
        val (webReq, _, _) = setupSuccessArgs()

        // and:
        SecurityContextHolder.getContext().authentication = null

        // when:
        val result = testResolveArgument(webReq)

        // then:
        assertNull(result)
    }

    @AfterEach
    fun teardown() {
        SecurityContextHolder.getContext().authentication = null
    }

    private fun setupSuccessArgs(): Triple<NativeWebRequest, Authentication, UserAgent> {
        // given:
        val webReq = mock(NativeWebRequest::class.java)
        val authObject = AuthenticationObjectImpl(
            JwtObjectBuilder.createRandom(), HttpAuthorizationTokenBuilder.createRandom()
        )
        val mockHttpReq = mockLocalhostServletRequest()
        val mockUa = UserAgent.create(
            randomEnumConst(PlatformType::class.java), getRandomAlphaNumericString(16), Version.COMPLIANCE
        )

        // when:
        SecurityContextHolder.getContext().authentication = authObject
        `when`(mockHttpReq.getHeader(UserAgent.HEADER_NAME)).thenReturn(
            "${mockUa.platformType.userAgentName}; ${mockUa.appVer}; ${mockUa.platformVer}"
        )
        `when`(webReq.nativeRequest).thenReturn(mockHttpReq)

        // then:
        return Triple(webReq, authObject, mockUa)
    }

    private fun testResolveArgument(webReq: NativeWebRequest): ClientAuthInfo? {
        val param = mock(MethodParameter::class.java)

        return sut.resolveArgument(param, null, webReq, null)
    }
}

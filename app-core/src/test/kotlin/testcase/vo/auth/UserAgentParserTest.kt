/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.vo.auth

import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.vo.auth.UserAgent
import com.github.fj.lib.util.getRandomAlphaNumericString
import de.skuzzle.semantic.Version
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.`when`
import test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest
import test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
class UserAgentParserTest {
    @ParameterizedTest(name = "Empty if 'User-Agent' is: '{0}'")
    @MethodSource("paramsForEmptyUserAgent")
    fun `empty if User-Agent header is these`(userAgent: String) {
        // given:
        val mockHttpReq = mockLocalhostServletRequest()

        // when:
        val ua = if (userAgent == "\\0") {
            null
        } else {
            userAgent
        }
        `when`(mockHttpReq.getHeader(UserAgent.HEADER_NAME)).thenReturn(ua)

        // then:
        val result = UserAgent.from(mockHttpReq)

        // expect:
        assertThat(result, `is`(UserAgent.EMPTY))
    }

    @ParameterizedTest(name = "User agent is '{1}' for \"'User-Agent': '{0}'\"")
    @MethodSource("paramsForUserAgent")
    fun `user agent is populated into model if parsable`(userAgent: String, expected: UserAgent) {
        // given:
        val mockHttpReq = mockLocalhostServletRequest()

        // when:
        `when`(mockHttpReq.getHeader(UserAgent.HEADER_NAME)).thenReturn(userAgent)

        // then:
        val result = UserAgent.from(mockHttpReq)

        // expect:
        assertThat(result, `is`(expected))
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun paramsForEmptyUserAgent(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("\\0"),
                Arguments.of(""),
                Arguments.of("asdfasdfasdf")
            )
        }

        @JvmStatic
        @Suppress("unused")
        fun paramsForUserAgent(): Stream<Arguments> {
            return Stream.of(
                randomEnumConst(PlatformType::class.java).let {
                    Arguments.of(it.userAgentName, UserAgent.create(it, "", Version.ZERO))
                },
                randomEnumConst(PlatformType::class.java).let {
                    Arguments.of("${it.userAgentName}; ${Version.COMPLIANCE}", UserAgent.create(it, "", Version.COMPLIANCE))
                },
                randomEnumConst(PlatformType::class.java).let {
                    val platformVer = getRandomAlphaNumericString(16)

                    return@let Arguments.of(
                        "${it.userAgentName}; ${Version.COMPLIANCE}; $platformVer",
                        UserAgent.create(it, platformVer, Version.COMPLIANCE)
                    )
                }
            )
        }
    }
}

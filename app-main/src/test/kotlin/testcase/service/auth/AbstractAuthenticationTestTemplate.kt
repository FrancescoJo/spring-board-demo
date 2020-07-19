/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

import com.github.fj.board.appconfig.CodecConfig
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.AuthenticationServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.annotation.CallSuper
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

/**
 * Collection of authentication related test fixture helpers.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
abstract class AbstractAuthenticationTestTemplate : AuthenticationServiceMixin {
    override val base62Codec = CodecConfig().base62()

    @Mock
    override lateinit var authTokenMgr: AuthTokenManager

    @Mock
    override lateinit var authProps: AppAuthProperties

    @Mock
    protected lateinit var authRepo: AuthenticationRepository

    @BeforeEach
    @CallSuper
    protected open fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    protected fun setupDefaultTokenGenerationStrategy(
        clientInfo: ClientAuthInfo,
        authentication: Authentication,
        timestamp: LocalDateTime
    ) {
        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS

        `when`(authRepo.findByLoginName(clientInfo.loginName)).thenReturn(authentication)
        `when`(authTokenMgr.create(anyString(), anyString(), any())).thenReturn(
            FreshHttpAuthorizationToken(getRandomAlphaNumericString(128), timestamp.plusSeconds(tokenLifespanSecs))
        )
        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)
    }
}

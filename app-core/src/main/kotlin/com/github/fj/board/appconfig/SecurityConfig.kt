/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.appconfig.security.AuthorizationHeaderFilter
import com.github.fj.board.appconfig.security.auth.HttpAuthenticationProvider
import com.github.fj.board.appconfig.security.web.AuthenticationEntryPointImpl
import com.github.fj.board.appconfig.security.web.AuthenticationFailureHandler
import com.github.fj.board.appconfig.security.web.SavedRequestAwareAuthenticationSuccessHandler
import com.github.fj.board.component.auth.AuthTokenManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableAspectJAutoProxy
class SecurityConfig @Inject constructor(
    private val tokenMgr: AuthTokenManager,
    /**
     * A series of Pair of Ant pattern URL and HTTP method information, to bypass security configuration.
     */
    @Autowired(required = false)
    @Qualifier(BYPASS_AUTH_URI_LIST)
    private val authCheckBypassUris: List<Pair<String, HttpMethod?>>?
) : WebSecurityConfigurerAdapter(), SecurityConfigMixin {
    // This logic is called only once.
    @Suppress("SpreadOperator")
    override fun configure(http: HttpSecurity) {
        val bypassUris = authCheckBypassUris.toRequestMatcher()

        http.addFilterBefore(AuthorizationHeaderFilter(bypassUris), BasicAuthenticationFilter::class.java)
            .authenticationProvider(tokenAuthProvider())
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint())
            .and()
            .authorizeRequests()
            .requestMatchers(*bypassUris.toTypedArray()).permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .successHandler(loginSuccessHandler())
            .failureHandler(loginFailureHandler())
            .and()
            .logout()
            .deleteCookies()
            .invalidateHttpSession(true)
    }

    @Bean
    fun tokenAuthProvider(): AuthenticationProvider = HttpAuthenticationProvider(LOG, tokenMgr)

    @Bean
    fun authEntryPoint(): AuthenticationEntryPoint = AuthenticationEntryPointImpl()

    @Bean
    fun loginSuccessHandler(): SimpleUrlAuthenticationSuccessHandler = SavedRequestAwareAuthenticationSuccessHandler()

    @Bean
    fun loginFailureHandler(): SimpleUrlAuthenticationFailureHandler = AuthenticationFailureHandler()

    /**
     * According to [org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration],
     * this configuration is must be provided to bypass Spring security default configuration.
     */
    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf(tokenAuthProvider()))
    }

    companion object {
        const val BYPASS_AUTH_URI_LIST = "SecurityConfig.authCheckBypassUris"

        private val LOG = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}

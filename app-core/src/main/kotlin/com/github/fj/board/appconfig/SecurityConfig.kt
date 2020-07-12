/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.appconfig.security.AuthorizationHeaderFilter
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.appconfig.security.HttpAuthenticationProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
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
    @Autowired(required = false) private val checkBypassUris: List<Pair<String, HttpMethod?>>?
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(tokenAuthProvider())
    }

    override fun configure(http: HttpSecurity) {
        val bypassUris = checkBypassUris.toRequestMatcher()

        http.addFilterBefore(AuthorizationHeaderFilter(bypassUris), BasicAuthenticationFilter::class.java)
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers(*bypassUris.toTypedArray()).permitAll()
    }

    @Bean
    fun tokenAuthProvider(): AuthenticationProvider = HttpAuthenticationProvider(LOG, tokenMgr)

    private fun List<Pair<String, HttpMethod?>>?.toRequestMatcher(): List<RequestMatcher> {
        if (this == null) {
            return emptyList()
        }

        return this.map { (antPath, httpMethod) ->
            return@map if (httpMethod == null) {
                AntPathRequestMatcher(antPath)
            } else {
                AntPathRequestMatcher(antPath, httpMethod.toString())
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}

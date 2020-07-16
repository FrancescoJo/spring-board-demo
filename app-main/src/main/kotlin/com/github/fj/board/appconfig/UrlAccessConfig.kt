/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.endpoint.ApiPaths
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@Configuration
class UrlAccessConfig {
    @Bean
    fun checkBypassUris(): List<Pair<String, HttpMethod?>> = listOf<Pair<String, HttpMethod?>>(
        // SignUp: SignUpController#signUp
        ApiPaths.ACCOUNT to HttpMethod.POST,
        // SignIn: SignUpController#signUp
        ApiPaths.ACCOUNT to HttpMethod.PATCH
    )
}

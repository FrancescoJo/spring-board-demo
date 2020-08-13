/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.endpoint.ApiPaths
import org.springframework.beans.factory.annotation.Qualifier
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
    @Qualifier(SecurityConfig.BYPASS_AUTH_URI_LIST)
    fun checkBypassUris(): List<Pair<String, HttpMethod?>> = listOf(
        // ERROR
        ApiPaths.ERROR to null,

        // SignUp: SignUpController#signUp
        ApiPaths.ACCOUNT to HttpMethod.POST,
        // SignIn: SignUpController#signUp
        ApiPaths.ACCOUNT to HttpMethod.PATCH,

        // Get Board: GetBoardController#getOne
        "${ApiPaths.BOARD}/**" to HttpMethod.GET,
        // Get Board: GetBoardController#getList
        ApiPaths.BOARDS to HttpMethod.GET,

        // Get Post: GetPostController#getOne
        "${ApiPaths.POST}/**" to HttpMethod.GET,
        // Get Post: GetPostController#getList
        "${ApiPaths.BOARD}/**/posts" to HttpMethod.GET,

        // Get Replies: GetRepliesController#getLatest
        "${ApiPaths.POST_ID}/**/replies/**" to HttpMethod.GET,
        // Get Replies: GetRepliesController#getLatest
        "${ApiPaths.POST_ID}/**/replies" to HttpMethod.GET
    )
}

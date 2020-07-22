/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import org.springframework.http.HttpMethod
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
internal interface SecurityConfigMixin {
    fun List<Pair<String, HttpMethod?>>?.toRequestMatcher(): List<RequestMatcher> {
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
}

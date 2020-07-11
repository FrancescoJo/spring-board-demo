/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig

import com.github.fj.board.component.property.AppAuthProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Oct - 2018
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app.authentication")
class AppAuthPropertiesConfig : AppAuthProperties {
    // @Value("\${app.authentication.rsa-key-alive-hours}")
    override var rsaKeyAliveHours: Long = AppAuthProperties.DEFAULT_RSA_KEY_ALIVE_HOURS

    // @Value("\${app.authentication.auth-token-alive-secs}")
    override var authTokenAliveSecs: Long = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS

    // @Value("\${app.authentication.refresh-token-alive-days}")
    override var refreshTokenAliveDays: Long = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS

    // @Value("\${app.authentication.token-issuer}")
    override var tokenIssuer: String = ""
}

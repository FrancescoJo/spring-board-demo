/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.security.Security

/**
 * Spring boot startup class
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@SpringBootApplication
@Suppress("UtilityClassWithPublicConstructor")
class Application {
    companion object {
        val profile: AppProfile by lazy {
            AppProfile.from(BuildConfig.profileName)
        }

        private val configurationNames = arrayOf("application", "application-" + profile.profileName)

        @JvmStatic
        fun main(args: Array<String>) {
            Security.addProvider(BouncyCastleProvider())

            // This logic is called only once.
            @Suppress("SpreadOperator")
            SpringApplicationBuilder(Application::class.java)
                .properties("spring.config.name:" + configurationNames.joinToString { it })
                .build()
                .run(*args)
        }
    }
}

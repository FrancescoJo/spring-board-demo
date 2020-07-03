/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Spring boot startup class
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
@SpringBootApplication
class Application {
    companion object {
        val profile: AppProfile by lazy {
            AppProfile.from(BuildConfig.profileName)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            @Suppress("SpreadOperator") // This logic is called only once.
            SpringApplicationBuilder(Application::class.java)
                .build()
                .run(*args)
        }
    }
}

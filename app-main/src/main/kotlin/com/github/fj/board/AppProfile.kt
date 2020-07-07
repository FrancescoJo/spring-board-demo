/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2018
 */
enum class AppProfile(val profileName: String) {
    LOCAL("local"),
    RELEASE("release");

    companion object {
        fun from(profileName: String) = values().firstOrNull { it.profileName == profileName } ?: LOCAL
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.auth

import com.github.fj.board.appconfig.controllerParam.ClientAuthInfoResolver
import com.github.fj.board.vo.auth.ClientAuthInfo

/**
 * Use this application component in controllers where [ClientAuthInfo] is required but optional.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 * @see ClientAuthInfoResolver
 */
interface ControllerClientAuthInfoDetector {
    fun detectClientAuthInfo(): ClientAuthInfo?
}

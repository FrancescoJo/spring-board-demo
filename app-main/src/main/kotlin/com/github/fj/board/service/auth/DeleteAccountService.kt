/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.vo.auth.ClientAuthInfo

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
interface DeleteAccountService {
    fun delete(clientInfo: ClientAuthInfo): Boolean
}

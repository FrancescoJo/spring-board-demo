/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board

import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
interface CloseBoardService : UserServiceMixin, BoardAuthorisationMixin {
    fun close(accessId: UUID, clientInfo: ClientAuthInfo): Boolean
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth.impl

import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.auth.DeleteAccountService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.stereotype.Service

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@Service
internal class DeleteAccountServiceImpl(
    private val authRepo: AuthenticationRepository,
    private val userRepo: UserRepository
) : DeleteAccountService {
    override fun delete(clientInfo: ClientAuthInfo): Boolean {
        TODO("Not yet implemented")
    }
}

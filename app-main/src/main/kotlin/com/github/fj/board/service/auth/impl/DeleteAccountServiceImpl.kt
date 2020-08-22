/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth.impl

import com.github.fj.board.exception.client.auth.CannotWithdrawException
import com.github.fj.board.persistence.model.auth.AuthenticationStatus
import com.github.fj.board.persistence.model.user.UserStatus
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.auth.DeleteAccountService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@Service
internal class DeleteAccountServiceImpl(
    private val authRepo: AuthenticationRepository,
    private val userRepo: UserRepository
) : DeleteAccountService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun delete(clientInfo: ClientAuthInfo): Boolean {
        val auth = authRepo.findByLoginName(clientInfo.loginName) ?: throw CannotWithdrawException()

        authRepo.save(auth.apply {
            status = AuthenticationStatus.DELETED
        })

        auth.user?.let {
            it.status = UserStatus.WITHDRAWN
            userRepo.save(it)
        }

        return true
    }
}

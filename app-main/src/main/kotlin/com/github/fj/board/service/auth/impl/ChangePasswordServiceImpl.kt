/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth.impl

import com.github.fj.board.endpoint.v1.auth.dto.ChangePasswordRequest
import com.github.fj.board.exception.client.DuplicatedPasswordException
import com.github.fj.board.exception.client.LoginNameNotFoundException
import com.github.fj.board.exception.client.WrongPasswordException
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.service.auth.ChangePasswordService
import com.github.fj.board.vo.auth.ClientRequestInfo
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
@Service
internal class ChangePasswordServiceImpl(
    private val authRepo: AuthenticationRepository
) : ChangePasswordService {
    @Transactional
    override fun changePassword(req: ChangePasswordRequest, clientInfo: ClientRequestInfo) {
        val auth = authRepo.findByLoginName(clientInfo.loginName) ?: run {
            // Guard case
            throw LoginNameNotFoundException()
        }

        when {
            !auth.isMatch(req.oldPassword) -> throw WrongPasswordException()
            auth.isMatch(req.newPassword)  -> throw DuplicatedPasswordException()
        }

        authRepo.save(auth.apply {
            password = hash(req.newPassword)
        })
    }
}

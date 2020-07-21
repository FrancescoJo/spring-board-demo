/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.Status
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.CloseBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@Service
internal class CloseBoardServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository
) : CloseBoardService {
    @Transactional
    override fun close(accessId: UUID, clientInfo: ClientAuthInfo): Boolean {
        val self = clientInfo.getCurrentUser()
        val board = accessId.getBoard().takeIf {
            it.status != Status.CLOSED
        } ?: throw BoardNotFoundException()

        self.assertAuthorityOf(board)

        boardRepo.save(board.apply {
            this.status = Status.CLOSED
            this.modifiedDate = utcNow()
        })

        return true
    }
}

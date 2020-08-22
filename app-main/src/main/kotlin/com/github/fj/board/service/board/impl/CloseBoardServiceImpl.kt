/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.CloseBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@Service
internal class CloseBoardServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository
) : CloseBoardService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun close(accessId: UUID, clientInfo: ClientAuthInfo): Boolean {
        val self = clientInfo.getCurrentAccessibleUser()
        val board = accessId.getBoard().takeIf {
            it.status != BoardStatus.CLOSED
        } ?: throw BoardNotFoundException()

        self.assertAuthorityOf(board)

        boardRepo.save(board.apply {
            this.status = BoardStatus.CLOSED
            this.modifiedDate = utcNow()
        })

        return true
    }
}

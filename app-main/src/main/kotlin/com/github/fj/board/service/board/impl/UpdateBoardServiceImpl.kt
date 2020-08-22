/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.endpoint.v1.board.request.UpdateBoardRequest
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.UpdateBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
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
internal class UpdateBoardServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository
) : UpdateBoardService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun update(accessId: UUID, req: UpdateBoardRequest, clientInfo: ClientAuthInfo): BoardInfo {
        val self = clientInfo.getCurrentAccessibleUser()
        val board = accessId.getBoard()
        self.assertAuthorityOf(board)

        val updatedBoard = board.apply {
            this.name = req.name
            this.access = req.access
            this.mode = req.mode
            this.description = req.description
            this.modifiedDate = utcNow()
        }

        return BoardInfo.from(updatedBoard, updatedBoard.getPostsCount()).also {
            boardRepo.save(updatedBoard)
        }
    }
}

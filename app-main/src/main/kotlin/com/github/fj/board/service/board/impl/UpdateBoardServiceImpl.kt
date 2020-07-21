/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequest
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.UpdateBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
@Service
class UpdateBoardServiceImpl(
    override val userRepo: UserRepository,
    private val boardRepo: BoardRepository
) : UpdateBoardService {
    @Transactional
    override fun update(accessId: UUID, req: UpdateBoardRequest, clientInfo: ClientAuthInfo): BoardInfo {
        val self = clientInfo.getCurrentUser() ?: run {
            throw UserNotFoundException()
        }

        val board = boardRepo.findByAccessId(accessId) ?: run {
            throw BoardNotFoundException()
        }

        if (self.id != board.creator.id) {
            throw UnauthorisedException()
        }

        val updatedBoard = board.apply {
            this.name = req.name
            this.description = req.description
            this.modifiedDate = utcNow()
        }

        return BoardInfo.from(updatedBoard).also {
            boardRepo.save(updatedBoard)
        }
    }
}

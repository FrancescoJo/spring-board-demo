/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.endpoint.v1.board.request.CreateBoardRequest
import com.github.fj.board.exception.client.board.DuplicatedBoardKeyException
import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.CreateBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.lib.time.utcNow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@Service
internal class CreateBoardServiceImpl(
    override val userRepo: UserRepository,
    private val boardRepo: BoardRepository
) : CreateBoardService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun create(req: CreateBoardRequest, clientInfo: ClientAuthInfo): BoardInfo {
        val self = clientInfo.getCurrentAccessibleUser()

        boardRepo.findByKey(req.key)?.let {
            throw DuplicatedBoardKeyException()
        }

        val now = utcNow()

        val createdBoard = Board().apply {
            accessId = UUID.randomUUID()
            status = BoardStatus.NORMAL
            access = req.access
            mode = req.mode
            key = req.key
            name = req.name
            description = req.description
            createdDate = now
            modifiedDate = now
            creator = self
        }

        return BoardInfo.from(createdBoard, 0).also {
            boardRepo.save(createdBoard)
        }
    }
}

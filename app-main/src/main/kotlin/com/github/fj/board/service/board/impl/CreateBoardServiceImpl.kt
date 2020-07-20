/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.board.impl

import com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequest
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.board.CreateBoardService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.board.BoardInfo
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
@Service
class CreateBoardServiceImpl(
    override val userRepo: UserRepository,
    private val boardRepo: BoardRepository
) : CreateBoardService {
    @Transactional
    override fun create(req: CreateBoardRequest, clientInfo: ClientAuthInfo): BoardInfo {
        TODO("Not yet implemented")
    }
}

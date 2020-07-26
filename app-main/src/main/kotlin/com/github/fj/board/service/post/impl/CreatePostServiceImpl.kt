/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post.impl

import com.github.fj.board.endpoint.v1.post.dto.CreatePostRequest
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.service.post.CreatePostService
import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.stereotype.Service

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
@Service
internal class CreatePostServiceImpl(
    override val userRepo: UserRepository,
    override val boardRepo: BoardRepository,
    private val postRepo: PostRepository
) : CreatePostService {
    override fun create(boardId: String, req: CreatePostRequest, clientInfo: ClientAuthInfo) {
        TODO("Not yet implemented")
    }
}

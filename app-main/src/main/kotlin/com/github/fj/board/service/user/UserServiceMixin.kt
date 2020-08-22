/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.user

import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.repository.user.UserRepository
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
interface UserServiceMixin {
    val userRepo: UserRepository

    /**
     * @return [User] object of currently authenticated peer. `null` if receiver [ClientAuthInfo] is invalid.
     */
    fun ClientAuthInfo.findCurrentAccessibleUser(): User? =
        /*
         * Without this method we have to declare EntityManager to manage JQL directly, but EntityManager
         * is generally not useful to services. If we can declare a `private val em: EntityManager` we don't need
         * this method exposed in UserRepository.
         *
         * Read https://discuss.kotlinlang.org/t/protected-functions-in-interfaces/2031 for details.
         */
        userRepo.findByLoginName(loginName)?.takeIf { it.status.isAccessible }

    /**
     * @return [User] object of currently authenticated peer.
     * @throws UserNotFoundException if receiver [ClientAuthInfo] is invalid.
     */
    @Throws(UserNotFoundException::class)
    fun ClientAuthInfo.getCurrentAccessibleUser(): User = findCurrentAccessibleUser() ?: throw UserNotFoundException()

    fun User.applyLastActivityWith(clientInfo: ClientAuthInfo, timestamp: LocalDateTime) {
        this.lastActiveDate = timestamp
        this.lastActiveIp = clientInfo.remoteAddr
        this.lastActivePlatformType = clientInfo.platformType
        this.lastActivePlatformVersion = clientInfo.platformVer
        this.lastActiveAppVersion = clientInfo.appVer
    }
}

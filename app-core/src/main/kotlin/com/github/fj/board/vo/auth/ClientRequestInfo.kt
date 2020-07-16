/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.auth

import com.github.fj.board.persistence.model.auth.PlatformType
import de.skuzzle.semantic.Version
import java.net.InetAddress

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
interface ClientRequestInfo {
    val loginName: String
    val remoteAddr: InetAddress
    val platformType: PlatformType
    val platformVer: String
    val appVer: Version

    companion object {
        data class Dto(
            override val loginName: String,
            override val remoteAddr: InetAddress,
            override val platformType: PlatformType,
            override val platformVer: String,
            override val appVer: Version
        ) : ClientRequestInfo

        fun create(
            loginName: String,
            remoteAddr: InetAddress,
            platformType: PlatformType,
            platformVer: String,
            appVer: Version
        ): ClientRequestInfo = Dto(
            loginName = loginName,
            remoteAddr = remoteAddr,
            platformType = platformType,
            platformVer = platformVer,
            appVer = appVer
        )
    }
}

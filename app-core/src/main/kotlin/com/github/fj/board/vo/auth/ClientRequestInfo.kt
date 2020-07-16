/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.auth

import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.util.extractInetAddress
import de.skuzzle.semantic.Version
import java.net.InetAddress
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
interface ClientRequestInfo : UserAgent {
    val loginName: String
    val remoteAddr: InetAddress

    companion object {
        private data class Dto(
            override val loginName: String,
            override val remoteAddr: InetAddress,
            override val platformType: PlatformType,
            override val platformVer: String,
            override val appVer: Version
        ) : ClientRequestInfo

        fun create(
            loginName: String,
            httpReq: HttpServletRequest
        ): ClientRequestInfo = with(UserAgent.from(httpReq)) {
            Dto(
                loginName = loginName,
                remoteAddr = httpReq.extractInetAddress(),
                platformType = platformType,
                platformVer = platformVer,
                appVer = appVer
            )
        }
    }
}

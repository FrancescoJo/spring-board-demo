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
interface ClientAuthInfo : UserAgent {
    val loginName: String
    val remoteAddr: InetAddress
    val requestLine: String

    companion object {
        private data class Dto(
            override val loginName: String,
            override val remoteAddr: InetAddress,
            override val requestLine: String,
            override val platformType: PlatformType,
            override val platformVer: String,
            override val appVer: Version
        ) : ClientAuthInfo

        fun create(
            loginName: String,
            httpReq: HttpServletRequest
        ): ClientAuthInfo = with(UserAgent.from(httpReq)) {
            Dto(
                loginName = loginName,
                remoteAddr = httpReq.extractInetAddress(),
                requestLine = "${httpReq.method} ${httpReq.requestURI}${httpReq.queryString?.let { "?$it" } ?: ""}",
                platformType = platformType,
                platformVer = platformVer,
                appVer = appVer
            )
        }
    }
}

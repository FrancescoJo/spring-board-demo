/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.endpoint.v1.auth.dto.AuthenticationRequest
import com.github.fj.board.exception.client.DuplicatedLoginNameException
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.util.extractInetAddress
import com.github.fj.board.vo.auth.AuthenticationResult
import com.github.fj.lib.security.toSha256Bytes
import com.github.fj.lib.time.utcNow
import io.seruco.encoding.base62.Base62
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
@Service
internal class SignUpServiceImpl(
    private val base62Encoder: Base62,
    private val authProps: AppAuthProperties,
    private val authRepo: AuthenticationRepository,
    private val authTokenMgr: AuthTokenManager
) : SignUpService {
    override fun signUp(req: AuthenticationRequest, httpReq: HttpServletRequest): AuthenticationResult {
        if (authRepo.findByLoginName(req.loginName) != null) {
            throw DuplicatedLoginNameException()
        }

        val now = utcNow()
        val auth = req.toAuthentication(now, httpReq.extractInetAddress())
        val token = authTokenMgr.create(auth.loginName, "", now)

        return AuthenticationResult(
            loginName = auth.loginName,
            accessToken = token.credentials,
            accessTokenExpiresAfter = token.expiration,
            refreshToken = String(base62Encoder.encode(auth.refreshToken)),
            refreshTokenExpiresAfter = auth.refreshTokenExpireAt
        ).also {
            authRepo.save(auth)
        }
    }

    private fun AuthenticationRequest.toAuthentication(now: LocalDateTime, ipAddr: InetAddress): Authentication {
        val req = this@toAuthentication

        return Authentication().apply {
            loginName = req.loginName
            password = req.password.value.toSha256Bytes()
            createdDate = now
            createdIp = ipAddr
            lastActiveDate = now
            lastActiveIp = ipAddr
            lastActivePlatformType = req.platformType
            lastActivePlatformVersion = req.platformVersion
            lastActiveAppVersion = req.appVersion

            createRefreshToken(now, authProps.refreshTokenAliveDays)
        }
    }
}

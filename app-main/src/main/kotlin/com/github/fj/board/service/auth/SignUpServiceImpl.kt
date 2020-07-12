/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.auth

import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest
import com.github.fj.board.exception.client.DuplicatedLoginNameException
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.repository.auth.AuthenticationRepository
import com.github.fj.board.util.extractInetAddress
import com.github.fj.board.vo.auth.SignUpResult
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
    override fun signUp(req: SignUpRequest, httpReq: HttpServletRequest): SignUpResult {
        if (authRepo.findByLoginName(req.loginName) != null) {
            throw DuplicatedLoginNameException()
        }

        val now = utcNow()
        val (token, auth) = req.toAuthentication(now, httpReq.extractInetAddress())

        return SignUpResult(
            loginName = auth.loginName,
            accessToken = token.credentials,
            accessTokenExpiresAfter = token.expiration,
            refreshToken = String(base62Encoder.encode(auth.refreshToken)),
            refreshTokenExpiresAfter = auth.refreshTokenExpireAt
        ).also {
            authRepo.save(auth)
        }
    }

    private fun SignUpRequest.toAuthentication(
        now: LocalDateTime,
        ipAddr: InetAddress
    ): Pair<FreshHttpAuthorizationToken, Authentication> {
        val req = this@toAuthentication
        val token = authTokenMgr.create(loginName, "", now)

        val auth = Authentication().apply {
            this.loginName = req.loginName
            this.password = req.password.value.toSha256Bytes()
            this.createdDate = now
            this.createdIp = ipAddr
            this.lastActiveDate = now
            this.lastActiveIp = ipAddr
            this.lastActivePlatformType = req.platformType
            this.lastActivePlatformVersion = req.platformVersion
            this.lastActiveAppVersion = req.appVersion

            this.createRefreshToken(now, authProps.refreshTokenAliveDays)
        }

        return Pair(token, auth)
    }
}

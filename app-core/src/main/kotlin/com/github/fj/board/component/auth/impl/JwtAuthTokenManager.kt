/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.auth.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fj.board.appconfig.security.auth.RsaKeyPairManager
import com.github.fj.board.component.auth.AuthTokenManager
import com.github.fj.board.component.auth.AuthTokenManager.Companion.LOG
import com.github.fj.board.component.property.AppAuthProperties
import com.github.fj.board.component.security.FreshHttpAuthorizationToken
import com.github.fj.board.component.security.HttpAuthorizationToken
import com.github.fj.board.exception.client.AccessTokenExpiredException
import com.github.fj.board.exception.client.AuthTokenException
import com.github.fj.board.exception.client.TokenValidationFailureException
import com.github.fj.lib.annotation.VisibleForTesting
import com.github.fj.lib.time.LOCAL_DATE_TIME_MIN
import com.github.fj.lib.time.utcEpochSecond
import com.github.fj.lib.time.utcLocalDateTimeOf
import com.github.fj.lib.time.utcNow
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.text.ParseException
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2018
 */
@Component
internal class JwtAuthTokenManager @Inject constructor(
    private val authProperties: AppAuthProperties,
    private val rsaKeyPairManager: RsaKeyPairManager,
    private val jsonMapper: ObjectMapper
) : AuthTokenManager {
    override fun create(audience: String, subject: String, timestamp: LocalDateTime): FreshHttpAuthorizationToken {
        val keyPair = rsaKeyPairManager.getLatest()

        val jwtObject = with(authProperties) {
            JwtObject(
                id = UUID.randomUUID(),
                issuer = tokenIssuer,
                subject = subject,
                audience = audience,
                expiration = timestamp.plusSeconds(authTokenAliveSecs),
                notBefore = NOT_BEFORE_THAN,
                issuedAt = timestamp
            )
        }
        val jwsHeader = JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT)
            .keyID(keyPair.keyId.toString()).build()

        val token = JWSObject(jwsHeader, jwtObject.toPayload()).run {
            sign(keyPair.rsaSigner)
            return@run serialize()
        }

        return FreshHttpAuthorizationToken(token, jwtObject.expiration)
    }

    override fun validate(token: HttpAuthorizationToken): Authentication {
        val jwtObject = try {
            parseToken(token)
        } catch (e: ParseException) {
            LOG.warn("Wrong jwt format")
            throw AuthTokenException(cause = e)
        } catch (e: JOSEException) {
            LOG.error("Error in RSA key retrieval logic")
            throw TokenValidationFailureException(cause = e)
        }

        with(jwtObject) {
            if (authProperties.tokenIssuer != issuer) {
                LOG.warn("Wrong issuer - {}", issuer)
                throw AuthTokenException()
            }

            if (issuedAt < notBefore) {
                LOG.warn("Wrong time constraint: {} < {}", issuedAt, notBefore)
                throw AuthTokenException()
            }

            val tolerance = utcNow().minusMinutes(EXPIRY_TOLERANCE_CLOCK_SKEW_MINS)

            if (tolerance > expiration) {
                LOG.debug("Accessing with expired token: {} > {}", tolerance, expiration)
                throw AccessTokenExpiredException()
            }
        }

        return AuthenticationObjectImpl(jwtObject, token)
    }

    @VisibleForTesting
    fun parseToken(token: HttpAuthorizationToken): JwtObject =
        with(JWSObject.parse(token.credentials)) {
            rsaKeyPairManager.getById(UUID.fromString(header.keyID)).let {
                if (!verify(it.rsaVerifier)) {
                    LOG.warn("RSA verification failure")
                    throw AuthTokenException()
                }
            }

            return@with payload.toJwtObject()
        }

    private fun Payload.toJwtObject(): JwtObject {
        val jsonStr = this.toString()
        val mapTypeRef = object : TypeReference<Map<String, Any>>() {}

        return jsonMapper.readValue(jsonStr, mapTypeRef).run {
            JwtObject(
                id = UUID.fromString(get(JWT_ID) as String),
                issuer = get(ISSUER) as String,
                subject = get(SUBJECT) as String,
                audience = get(AUDIENCE) as String,
                expiration = utcLocalDateTimeOf(get(EXPIRATION) as Int),
                notBefore = utcLocalDateTimeOf(get(NOT_BEFORE) as Int),
                issuedAt = utcLocalDateTimeOf(get(ISSUED_AT) as Int)
            )
        }
    }

    private fun JwtObject.toPayload(): Payload = Payload(
        jsonMapper.writeValueAsString(
            jsonMapper.createObjectNode().apply {
                put(JWT_ID, id.toString())
                put(ISSUER, issuer)
                put(SUBJECT, subject)

                put(AUDIENCE, audience)
                put(EXPIRATION, expiration.utcEpochSecond())
                put(NOT_BEFORE, notBefore.utcEpochSecond())
                put(ISSUED_AT, issuedAt.utcEpochSecond())
            })
    )

    companion object {
        @VisibleForTesting
        val NOT_BEFORE_THAN = LOCAL_DATE_TIME_MIN

        @VisibleForTesting
        val EXPIRY_TOLERANCE_CLOCK_SKEW_MINS = 5L

        private const val JWT_ID = "jti"
        private const val ISSUER = "iss"
        private const val SUBJECT = "sub"
        private const val AUDIENCE = "aud"
        private const val EXPIRATION = "exp"
        private const val NOT_BEFORE = "nbf"
        private const val ISSUED_AT = "iat"
    }
}

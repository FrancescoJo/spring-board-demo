/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.fj.board.persistence.entity.auth.Authentication
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.validation.ProtectedPropertySize
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.util.ProtectedProperty
import de.skuzzle.semantic.Version
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.Pattern

/**
 * A sign-up request to create both Authentication and User object.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@JsonDeserialize
data class AuthenticationRequest(
    @JsonProperty
    @JsonPropertyDescription(DESC_LOGIN_NAME)
    @get:Pattern(
        regexp = "^[A-Za-z0-9]{$LOGIN_NAME_SIZE_MIN,$LOGIN_NAME_SIZE_MAX}\$",
        message = "`loginName` must between $LOGIN_NAME_SIZE_MIN and $LOGIN_NAME_SIZE_MAX alphanumeric characters."
    )
    val loginName: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_PASSWORD)
    @get:ProtectedPropertySize(
        min = PASSWORD_SIZE,
        max = PASSWORD_SIZE,
        message = "`password` must be hex encoded, 40 characters long."
    )
    val password: ProtectedProperty<String>,

    @JsonProperty
    @JsonPropertyDescription(DESC_PLATFORM_TYPE)
    val platformType: PlatformType,

    @JsonProperty
    @JsonPropertyDescription(DESC_PLATFORM_VERSION)
    val platformVersion: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_APP_VERSION)
    val appVersion: Version
) {
    fun createClientAuthInfoBy(httpReq: HttpServletRequest) = ClientAuthInfo.create(loginName, httpReq)

    companion object {
        const val LOGIN_NAME_SIZE_MIN = Authentication.LOGIN_NAME_SIZE_MIN
        const val LOGIN_NAME_SIZE_MAX = Authentication.LOGIN_NAME_SIZE_MAX
        const val PASSWORD_SIZE = Authentication.PASSWORD_SIZE

        const val DESC_LOGIN_NAME = "A unique login name to distinguish users. " +
                "Must between $LOGIN_NAME_SIZE_MIN and $LOGIN_NAME_SIZE_MAX alphanumeric characters."
        const val DESC_PASSWORD = "A sha-1 encoded password to get access to service. Must be hex encoded, " +
                "40 characters long. Do not send any plaintext for this field."
        const val DESC_PLATFORM_TYPE = "Client platform type. Read link:#common-types-platformType[`PlatformType`] " +
                "for more details."
        const val DESC_PLATFORM_VERSION = "Version string of client system."
        const val DESC_APP_VERSION = "Semantic versioning format encoded client app version. "
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.validation.ProtectedPropertySize
import com.github.fj.lib.util.ProtectedProperty
import de.skuzzle.semantic.Version
import javax.validation.constraints.Size

/**
 * A sign-up request to create both Authentication and User object.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Jun - 2020
 */
@JsonDeserialize
data class SignUpRequest(
    @JsonProperty
    @Size(
        min = LOGIN_NAME_SIZE_MIN,
        max = LOGIN_NAME_SIZE_MAX,
        message = "loginName must between $LOGIN_NAME_SIZE_MIN to $LOGIN_NAME_SIZE_MAX alphanumeric characters."
    )
    val loginName: String,

    @JsonProperty
    @ProtectedPropertySize(
        min = PASSWORD_SIZE_MIN,
        max = PASSWORD_SIZE_MAX,
        message = "password must between $PASSWORD_SIZE_MIN to $PASSWORD_SIZE_MAX characters."
    )
    val password: ProtectedProperty<String>,

    /**
     * Client platform type
     */
    @JsonProperty
    val platformType: PlatformType,

    /**
     * Client platform version
     */
    @JsonProperty
    val platformVersion: String,

    /**
     * Client app version, encoded by Semantic versioning format
     */
    @JsonProperty
    val appVersion: Version
) {
    companion object {
        const val LOGIN_NAME_SIZE_MIN = 4
        const val LOGIN_NAME_SIZE_MAX = 16
        const val PASSWORD_SIZE_MIN = 6
        const val PASSWORD_SIZE_MAX = 32
    }
}

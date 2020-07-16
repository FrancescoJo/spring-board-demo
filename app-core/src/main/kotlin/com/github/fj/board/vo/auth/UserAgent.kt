/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.auth

import com.github.fj.board.persistence.model.auth.PlatformType
import de.skuzzle.semantic.Version
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Jul - 2020
 */
interface UserAgent {
    val platformType: PlatformType
    val platformVer: String
    val appVer: Version

    companion object {
        const val HEADER_NAME = "User-Agent"

        val EMPTY: UserAgent = Impl(PlatformType.UNDEFINED, "", Version.ZERO)

        fun from(req: HttpServletRequest): UserAgent {
            val header = req.getHeader(HEADER_NAME)?.takeIf { it.isNotEmpty() } ?: return EMPTY

            @Suppress("MagicNumber")
            with(header.split(";")) {
                val platformType = PlatformType.byUserAgentName(getTrimmedAt(0))
                val appVer = try {
                    Version.parseVersion(getTrimmedAt(1))
                } catch (e: Version.VersionFormatException) {
                    Version.ZERO
                }
                val platformVer = getTrimmedAt(2)

                return Impl(platformType, platformVer, appVer)
            }
        }

        private fun List<String>.getTrimmedAt(idx: Int) = getOrNull(idx)?.trim() ?: ""

        internal fun create(
            platformType: PlatformType,
            platformVer: String,
            appVer: Version
        ): UserAgent = Impl(
            platformType = platformType,
            platformVer = platformVer,
            appVer = appVer
        )

        private data class Impl(
            override val platformType: PlatformType,
            override val platformVer: String,
            override val appVer: Version
        ) : UserAgent
    }
}

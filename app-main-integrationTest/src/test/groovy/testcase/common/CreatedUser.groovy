/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.common

import com.github.fj.board.persistence.model.user.UserStatus

import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class CreatedUser {
    final String loginName
    final String accessToken
    final LocalDateTime accessTokenExpiresAfter
    final String refreshToken
    final LocalDateTime refreshTokenExpiresAfter
    final String nickname
    final UserStatus status
    final String email
    final LocalDateTime createdDate
    final LocalDateTime lastActiveDate

    CreatedUser(
            final String loginName,
            final String accessToken,
            final LocalDateTime accessTokenExpiresAfter,
            final String refreshToken,
            final LocalDateTime refreshTokenExpiresAfter,
            final String nickname,
            final UserStatus status,
            final String email,
            final LocalDateTime createdDate,
            final LocalDateTime lastActiveDate
    ) {
        this.loginName = loginName
        this.accessToken = accessToken
        this.accessTokenExpiresAfter = accessTokenExpiresAfter
        this.refreshToken = refreshToken
        this.refreshTokenExpiresAfter = refreshTokenExpiresAfter
        this.nickname = nickname
        this.status = status
        this.email = email
        this.createdDate = createdDate
        this.lastActiveDate = lastActiveDate
    }

    @Override
    String toString() {
        return "CreatedUser{" +
                "loginName='$loginName'" +
                ", accessToken='$accessToken'" +
                ", accessTokenExpiresAfter=$accessTokenExpiresAfter" +
                ", refreshToken='$refreshToken" +
                ", refreshTokenExpiresAfter=$refreshTokenExpiresAfter" +
                ", nickname='$nickname'" +
                ", status='$status'" +
                ", email='$email'" +
                ", createdDate='$createdDate'" +
                ", lastActiveDate='$lastActiveDate'" +
                '}'
    }
}

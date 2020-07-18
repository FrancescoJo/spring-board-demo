/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.auth;

import com.github.fj.board.persistence.entity.auth.Authentication;
import com.github.fj.lib.security.HashUtilsKt;
import com.github.fj.lib.time.DateTimeUtilsKt;

import java.time.LocalDateTime;

import static com.github.fj.lib.util.RandomUtilsKt.*;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
public class AuthenticationBuilder {
    private long id = 0L;
    private String loginName = "";
    private byte[] password = new byte[32];
    private byte[] refreshToken = new byte[0];
    private LocalDateTime refreshTokenIssuedAt = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private LocalDateTime refreshTokenExpireAt = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();

    public AuthenticationBuilder() {
    }

    public AuthenticationBuilder(final Authentication src) {
        if (src == null) {
            return;
        }

        this.id = src.getId();
        this.loginName = src.getLoginName();
        this.password = src.getPassword();
        this.refreshToken = src.getRefreshToken();
        this.refreshTokenIssuedAt = src.getRefreshTokenIssuedAt();
        this.refreshTokenExpireAt = src.getRefreshTokenExpireAt();
    }

    public AuthenticationBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public AuthenticationBuilder loginName(final String loginName) {
        this.loginName = loginName;
        return this;
    }

    public AuthenticationBuilder password(final String password) {
        this.password = HashUtilsKt.toSha256Bytes(password);
        return this;
    }

    public AuthenticationBuilder refreshToken(final byte[] refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public AuthenticationBuilder refreshTokenIssuedAt(final LocalDateTime refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
        return this;
    }

    public AuthenticationBuilder refreshTokenExpireAt(final LocalDateTime refreshTokenExpireAt) {
        this.refreshTokenExpireAt = refreshTokenExpireAt;
        return this;
    }

    public Authentication build() {
        final Authentication object = new Authentication();

        object.setId(id);
        object.setLoginName(loginName);
        object.setPassword(password);
        object.setRefreshToken(refreshToken);
        object.setRefreshTokenIssuedAt(refreshTokenIssuedAt);
        object.setRefreshTokenExpireAt(refreshTokenExpireAt);

        return object;
    }

    public static Authentication createRandom() {
        final LocalDateTime now = DateTimeUtilsKt.utcNow();

        return new AuthenticationBuilder()
                .id(getRandomPositiveLong(0, Long.MAX_VALUE))
                .loginName(getRandomAlphaNumericString(
                        getRandomPositiveInt(Authentication.LOGIN_NAME_SIZE_MIN, Authentication.LOGIN_NAME_SIZE_MAX)
                ))
                .password(getRandomAlphaNumericString(32))
                .refreshToken(getSecureRandomBytes(Authentication.REFRESH_TOKEN_LENGTH_BYTES))
                .refreshTokenIssuedAt(now)
                .refreshTokenExpireAt(now.plusDays(180))
                .build();
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.auth;

import com.github.fj.board.persistence.entity.auth.Authentication;
import com.github.fj.board.persistence.model.auth.PlatformType;
import com.github.fj.lib.net.InetAddressExtensions;
import com.github.fj.lib.security.HashUtilsKt;
import com.github.fj.lib.time.DateTimeUtilsKt;
import de.skuzzle.semantic.Version;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import static com.github.fj.lib.util.RandomUtilsKt.*;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
public class AuthenticationBuilder {
    private long id = 0L;
    private String loginName = "";
    private byte[] password = new byte[32];
    private LocalDateTime createdDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private InetAddress createdIp = InetAddressExtensions.INSTANCE.getEMPTY_INET_ADDRESS();
    private LocalDateTime lastActiveDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private InetAddress lastActiveIp = InetAddressExtensions.INSTANCE.getEMPTY_INET_ADDRESS();
    private PlatformType lastActivePlatformType = PlatformType.UNDEFINED;
    private String lastActivePlatformVersion = "";
    private Version lastActiveAppVersion = Version.ZERO;
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
        this.createdDate = src.getCreatedDate();
        this.createdIp = src.getCreatedIp();
        this.lastActiveDate = src.getLastActiveDate();
        this.lastActiveIp = src.getLastActiveIp();
        this.lastActivePlatformType = src.getLastActivePlatformType();
        this.lastActivePlatformVersion = src.getLastActivePlatformVersion();
        this.lastActiveAppVersion = src.getLastActiveAppVersion();
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

    public AuthenticationBuilder createdDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public AuthenticationBuilder createdIp(final InetAddress createdIp) {
        this.createdIp = createdIp;
        return this;
    }

    public AuthenticationBuilder lastActiveDate(final LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
        return this;
    }

    public AuthenticationBuilder lastActiveIp(final InetAddress lastActiveIp) {
        this.lastActiveIp = lastActiveIp;
        return this;
    }

    public AuthenticationBuilder lastActivePlatformType(final PlatformType lastActivePlatformType) {
        this.lastActivePlatformType = lastActivePlatformType;
        return this;
    }

    public AuthenticationBuilder lastActivePlatformVersion(final String lastActivePlatformVersion) {
        this.lastActivePlatformVersion = lastActivePlatformVersion;
        return this;
    }

    public AuthenticationBuilder lastActiveAppVersion(final Version lastActiveAppVersion) {
        this.lastActiveAppVersion = lastActiveAppVersion;
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
        object.setCreatedDate(createdDate);
        object.setCreatedIp(createdIp);
        object.setLastActiveDate(lastActiveDate);
        object.setLastActiveIp(lastActiveIp);
        object.setLastActivePlatformType(lastActivePlatformType);
        object.setLastActivePlatformVersion(lastActivePlatformVersion);
        object.setLastActiveAppVersion(lastActiveAppVersion);
        object.setRefreshToken(refreshToken);
        object.setRefreshTokenIssuedAt(refreshTokenIssuedAt);
        object.setRefreshTokenExpireAt(refreshTokenExpireAt);

        return object;
    }

    public static Authentication createRandom() throws UnknownHostException {
        final LocalDateTime now = DateTimeUtilsKt.utcNow();
        final InetAddress localhost = InetAddress.getLocalHost();

        return new AuthenticationBuilder()
                .id(getRandomPositiveLong(0, Long.MAX_VALUE))
                .loginName(getRandomAlphaNumericString(
                        getRandomPositiveInt(Authentication.LOGIN_NAME_SIZE_MIN, Authentication.LOGIN_NAME_SIZE_MAX)
                ))
                .password(getRandomAlphaNumericString(32))
                .createdDate(now)
                .createdIp(localhost)
                .lastActiveDate(now)
                .lastActiveIp(localhost)
                .lastActivePlatformType(randomEnumConst(PlatformType.class))
                .lastActivePlatformVersion("")
                .lastActiveAppVersion(Version.ZERO)
                .refreshToken(getSecureRandomBytes(Authentication.REFRESH_TOKEN_LENGTH_BYTES))
                .refreshTokenIssuedAt(now)
                .refreshTokenExpireAt(now.plusDays(180))
                .build();
    }
}

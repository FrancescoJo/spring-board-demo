/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.user;

import com.github.fj.board.persistence.entity.auth.Authentication;
import com.github.fj.board.persistence.entity.user.User;
import com.github.fj.board.persistence.model.auth.PlatformType;
import com.github.fj.board.persistence.model.user.UserStatus;
import com.github.fj.lib.net.InetAddressExtensions;
import com.github.fj.lib.time.DateTimeUtilsKt;
import de.skuzzle.semantic.Version;
import test.com.github.fj.board.persistence.entity.auth.AuthenticationBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveLong;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEmail;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
public class UserBuilder {
    private long id = 0L;
    private Authentication authentication = null;
    private String nickname = "";
    private UserStatus status = UserStatus.UNVERIFIED;
    private String email = "";
    private LocalDateTime createdDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private InetAddress createdIp = InetAddressExtensions.INSTANCE.getEMPTY_INET_ADDRESS();
    private LocalDateTime lastActiveDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private InetAddress lastActiveIp = InetAddressExtensions.INSTANCE.getEMPTY_INET_ADDRESS();
    private PlatformType lastActivePlatformType = PlatformType.UNDEFINED;
    private String lastActivePlatformVersion = "";
    private Version lastActiveAppVersion = Version.ZERO;
    private User invitedBy = null;

    public UserBuilder() {
    }

    public UserBuilder(final User src) {
        if (src == null) {
            return;
        }

        this.id = src.getId();
        this.authentication = src.getAuthentication();
        this.nickname = src.getNickname();
        this.status = src.getStatus();
        this.email = src.getEmail();
        this.createdDate = src.getCreatedDate();
        this.createdIp = src.getCreatedIp();
        this.lastActiveDate = src.getLastActiveDate();
        this.lastActiveIp = src.getLastActiveIp();
        this.lastActivePlatformType = src.getLastActivePlatformType();
        this.lastActivePlatformVersion = src.getLastActivePlatformVersion();
        this.lastActiveAppVersion = src.getLastActiveAppVersion();
        this.invitedBy = src.getInvitedBy();
    }

    public UserBuilder id(final long id) {
        this.id = id;
        return this;
    }

    public UserBuilder authentication(final @Nullable Authentication value) {
        this.authentication = value;
        return this;
    }

    public UserBuilder nickname(final @Nonnull String value) {
        this.nickname = value;
        return this;
    }

    public UserBuilder status(final @Nonnull UserStatus value) {
        this.status = value;
        return this;
    }

    public UserBuilder email(final @Nonnull String value) {
        this.email = value;
        return this;
    }

    public UserBuilder createdDate(final @Nonnull LocalDateTime value) {
        this.createdDate = value;
        return this;
    }

    public UserBuilder createdIp(final @Nonnull InetAddress value) {
        this.createdIp = value;
        return this;
    }

    public UserBuilder lastActiveDate(final @Nonnull LocalDateTime value) {
        this.lastActiveDate = value;
        return this;
    }

    public UserBuilder lastActiveIp(final @Nonnull InetAddress value) {
        this.lastActiveIp = value;
        return this;
    }

    public UserBuilder lastActivePlatformType(final @Nonnull PlatformType value) {
        this.lastActivePlatformType = value;
        return this;
    }

    public UserBuilder lastActivePlatformVersion(final @Nonnull String value) {
        this.lastActivePlatformVersion = value;
        return this;
    }

    public UserBuilder lastActiveAppVersion(final @Nonnull Version value) {
        this.lastActiveAppVersion = value;
        return this;
    }

    public UserBuilder invitedBy(final @Nullable User value) {
        this.invitedBy = value;
        return this;
    }

    public User build() {
        final User object = new User();

        object.setId(id);
        object.setAuthentication(authentication);
        object.setNickname(nickname);
        object.setStatus(status);
        object.setEmail(email);
        object.setCreatedDate(createdDate);
        object.setCreatedIp(createdIp);
        object.setLastActiveDate(lastActiveDate);
        object.setLastActiveIp(lastActiveIp);
        object.setLastActivePlatformType(lastActivePlatformType);
        object.setLastActivePlatformVersion(lastActivePlatformVersion);
        object.setLastActiveAppVersion(lastActiveAppVersion);
        object.setInvitedBy(invitedBy);

        return object;
    }

    public static User createRandom() throws UnknownHostException {
        return createRandom(null);
    }

    public static User createRandom(final @Nullable User invitedBy) throws UnknownHostException {
        final LocalDateTime now = DateTimeUtilsKt.utcNow();
        final InetAddress localhost = InetAddress.getLocalHost();

        return new UserBuilder()
                .id(getRandomPositiveLong(0, Long.MAX_VALUE))
                .authentication(AuthenticationBuilder.createRandom())
                .nickname(getRandomAlphaNumericString(8))
                .status(randomEnumConst(UserStatus.class))
                .email(randomEmail())
                .createdDate(now)
                .createdIp(localhost)
                .lastActiveDate(now)
                .lastActiveIp(localhost)
                .lastActivePlatformType(randomEnumConst(PlatformType.class))
                .lastActivePlatformVersion("")
                .lastActiveAppVersion(Version.ZERO)
                .invitedBy(invitedBy)
                .build();
    }
}

/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.component.auth;

import com.github.fj.board.component.auth.impl.JwtObject;
import com.github.fj.board.component.property.AppAuthProperties;
import com.github.fj.lib.time.DateTimeUtilsKt;
import com.github.fj.lib.util.UuidExtensions;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.fj.lib.time.DateTimeUtilsKt.utcNow;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
public class JwtObjectBuilder {
    private UUID id = UuidExtensions.INSTANCE.getEMPTY_UUID();
    private String issuer = "";
    private String subject = "";
    private String audience = "";
    private LocalDateTime expiration = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private LocalDateTime notBefore = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private LocalDateTime issuedAt = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();

    public JwtObjectBuilder id(final UUID value) {
        this.id = value;
        return this;
    }

    public JwtObjectBuilder issuer(final String value) {
        this.issuer = value;
        return this;
    }

    public JwtObjectBuilder subject(final String value) {
        this.subject = value;
        return this;
    }

    public JwtObjectBuilder audience(final String value) {
        this.audience = value;
        return this;
    }

    public JwtObjectBuilder expiration(final LocalDateTime value) {
        this.expiration = value;
        return this;
    }

    public JwtObjectBuilder notBefore(final LocalDateTime value) {
        this.notBefore = value;
        return this;
    }

    public JwtObjectBuilder issuedAt(final LocalDateTime value) {
        this.issuedAt = value;
        return this;
    }

    public JwtObject build() {
        return new JwtObject(
                /* id */ id,
                /* issuer */ issuer,
                /* subject */ subject,
                /* audience */ audience,
                /* expiration */ expiration,
                /* notBefore */ notBefore,
                /* issuedAt */ issuedAt
        );
    }

    public static JwtObject createRandom() {
        final LocalDateTime now = utcNow();

        return new JwtObjectBuilder()
                .id(UUID.randomUUID())
                .issuer(getRandomAlphaNumericString(16))
                .subject(getRandomAlphaNumericString(8))
                .audience(getRandomAlphaNumericString(4))
                .expiration(now.plusSeconds(AppAuthProperties.Companion.getDEFAULT_AUTH_TOKEN_ALIVE_SECS()))
                .notBefore(DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN())
                .issuedAt(now)
                .build();
    }
}

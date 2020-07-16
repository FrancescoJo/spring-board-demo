/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.component.security;

import com.github.fj.board.component.property.AppAuthProperties;
import com.github.fj.board.component.security.FreshHttpAuthorizationToken;
import com.github.fj.board.component.security.HttpAuthScheme;
import com.github.fj.board.component.security.HttpAuthorizationToken;
import com.github.fj.board.component.security.MaybeHttpAuthorizationToken;

import static com.github.fj.lib.time.DateTimeUtilsKt.utcNow;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
public class HttpAuthorizationTokenBuilder {
    private HttpAuthScheme scheme = HttpAuthScheme.UNDEFINED;
    private String token = "";

    public HttpAuthorizationTokenBuilder scheme(final HttpAuthScheme value) {
        this.scheme = value;
        return this;
    }

    public HttpAuthorizationTokenBuilder token(final String value) {
        this.token = value;
        return this;
    }

    public HttpAuthorizationToken build() {
        if (scheme == HttpAuthScheme.TOKEN) {
            final long lifespan = AppAuthProperties.Companion.getDEFAULT_AUTH_TOKEN_ALIVE_SECS();
            return new FreshHttpAuthorizationToken(token, utcNow().plusSeconds(lifespan));
        } else {
            return new MaybeHttpAuthorizationToken(scheme, token);
        }
    }

    public static HttpAuthorizationToken createRandom() {
        return new HttpAuthorizationTokenBuilder()
                .scheme(randomEnumConst(HttpAuthScheme.class))
                .token(getRandomAlphaNumericString(128))
                .build();
    }
}

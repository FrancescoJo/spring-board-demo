/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.auth.dto;

import com.github.fj.board.endpoint.v1.auth.request.RefreshTokenRequest;
import com.github.fj.lib.util.ProtectedProperty;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Jul - 2020
 */
public final class RefreshTokenRequestBuilder {
    private String refreshToken;

    public RefreshTokenRequestBuilder() {
    }

    public RefreshTokenRequestBuilder(final RefreshTokenRequest src) {
        this.refreshToken = src.getOldRefreshToken().getValue();
    }

    public RefreshTokenRequestBuilder refreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public RefreshTokenRequest build() {
        return new RefreshTokenRequest(
                /* oldRefreshToken */ new ProtectedProperty<>(refreshToken)
        );
    }

    public static RefreshTokenRequest createRandom() {
        return new RefreshTokenRequestBuilder()
                .refreshToken(getRandomAlphaNumericString(32))
                .build();
    }
}

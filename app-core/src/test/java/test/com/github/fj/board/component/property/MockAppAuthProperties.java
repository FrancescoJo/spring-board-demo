/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.component.property;

import com.github.fj.board.component.property.AppAuthProperties;

import javax.annotation.Nonnull;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Nov - 2018
 */
public class MockAppAuthProperties implements AppAuthProperties {
    private long rsaKeyAliveHours = 0L;
    private long authTokenAliveSecs = 0L;
    private long refreshTokenAliveDays = 0L;
    private String tokenIssuer = "";

    @Override
    public long getRsaKeyAliveHours() {
        return rsaKeyAliveHours;
    }

    public void setRsaKeyAliveHours(final long value) {
        this.rsaKeyAliveHours = value;
    }

    @Override
    public long getAuthTokenAliveSecs() {
        return authTokenAliveSecs;
    }

    public void setAuthTokenAliveSecs(final long value) {
        this.authTokenAliveSecs = value;
    }

    @Override
    public long getRefreshTokenAliveDays() {
        return refreshTokenAliveDays;
    }

    public void setRefreshTokenAliveDays(final long value) {
        this.refreshTokenAliveDays = value;
    }

    @Nonnull
    @Override
    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public void setTokenIssuer(final String value) {
        this.tokenIssuer = value;
    }
}

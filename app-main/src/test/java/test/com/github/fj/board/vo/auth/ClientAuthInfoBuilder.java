/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.vo.auth;

import com.github.fj.board.vo.auth.ClientAuthInfo;

import javax.servlet.http.HttpServletRequest;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
public final class ClientAuthInfoBuilder {
    private String loginName = "";
    private HttpServletRequest httpServletRequest = mockLocalhostServletRequest();

    public ClientAuthInfoBuilder loginName(final String value) {
        this.loginName = value;
        return this;
    }

    public ClientAuthInfoBuilder httpServletRequest(final HttpServletRequest value) {
        this.httpServletRequest = value;
        return this;
    }

    public ClientAuthInfo build() {
        return ClientAuthInfo.Companion.create(loginName, httpServletRequest);
    }

    public static ClientAuthInfo createRandom() {
        return new ClientAuthInfoBuilder()
                .loginName(getRandomAlphaNumericString(8))
                .httpServletRequest(mockLocalhostServletRequest())
                .build();
    }
}

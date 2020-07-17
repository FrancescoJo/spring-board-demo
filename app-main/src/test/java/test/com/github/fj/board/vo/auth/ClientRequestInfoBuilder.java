/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.vo.auth;

import com.github.fj.board.vo.auth.ClientRequestInfo;

import javax.servlet.http.HttpServletRequest;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.board.util.HttpRequestUtils.mockLocalhostServletRequest;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
public final class ClientRequestInfoBuilder {
    private String loginName = "";
    private HttpServletRequest httpServletRequest = mockLocalhostServletRequest();

    public ClientRequestInfoBuilder loginName(final String value) {
        this.loginName = value;
        return this;
    }

    public ClientRequestInfoBuilder httpServletRequest(final HttpServletRequest value) {
        this.httpServletRequest = value;
        return this;
    }

    public ClientRequestInfo build() {
        return ClientRequestInfo.Companion.create(loginName, httpServletRequest);
    }

    public static ClientRequestInfo createRandom() {
        return new ClientRequestInfoBuilder()
                .loginName(getRandomAlphaNumericString(8))
                .httpServletRequest(mockLocalhostServletRequest())
                .build();
    }
}

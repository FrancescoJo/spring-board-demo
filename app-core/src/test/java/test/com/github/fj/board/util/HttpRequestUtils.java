/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.util;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jul - 2020
 */
public class HttpRequestUtils {
    public static HttpServletRequest mockLocalhostServletRequest() {
        final HttpServletRequest mockServletRequest = mock(HttpServletRequest.class);

        when(mockServletRequest.getRemoteAddr()).thenReturn("localhost");

        return mockServletRequest;
    }
}

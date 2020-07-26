/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.endpoint;

import com.github.fj.board.endpoint.ApiPaths;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
public class ApiPathsHelper {
    /**
     * {@link com.github.fj.board.endpoint.ApiPaths#USER_NICKNAME}
     */
    public static String USER_NICKNAME(final String nickname) {
        return ApiPaths.USER + "/" + nickname;
    }

    /**
     * {@link com.github.fj.board.endpoint.ApiPaths#BOARD_ID}
     */
    public static String BOARD_ID(final String accessId) {
        return ApiPaths.BOARD + "/" + accessId;
    }
}

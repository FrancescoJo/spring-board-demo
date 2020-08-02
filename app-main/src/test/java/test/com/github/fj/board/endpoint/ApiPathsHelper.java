/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint;

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

    /**
     * {@link com.github.fj.board.endpoint.ApiPaths#BOARD_ID_POST}
     */
    public static String BOARD_ID_POST(final String accessId) {
        return ApiPaths.BOARD + "/" + accessId + "/post";
    }

    /**
     * {@link com.github.fj.board.endpoint.ApiPaths#BOARD_ID_POST_ID}
     */
    public static String BOARD_ID_POST_ID(final String boardAccessId, final String postAccessId) {
        return ApiPaths.BOARD + "/" + boardAccessId + "/post/" + postAccessId;
    }
}

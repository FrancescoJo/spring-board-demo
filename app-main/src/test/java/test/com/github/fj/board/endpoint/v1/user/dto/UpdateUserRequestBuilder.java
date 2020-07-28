/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.user.dto;

import com.github.fj.board.endpoint.v1.user.request.UpdateUserRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.randomBoolean;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEmail;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
public final class UpdateUserRequestBuilder {
    private String nickname = "";
    private String email = null;

    public UpdateUserRequestBuilder() {
    }

    public UpdateUserRequestBuilder(final UpdateUserRequest src) {
        this.nickname = src.getNickname();
        this.email = src.getEmail();
    }

    public UpdateUserRequestBuilder nickname(final @Nonnull String value) {
        this.nickname = value;
        return this;
    }

    public UpdateUserRequestBuilder email(final @Nullable String value) {
        this.email = value;
        return this;
    }

    public UpdateUserRequest build() {
        return new UpdateUserRequest(
                /* nickname */  nickname,
                /* email */     email
        );
    }

    public static UpdateUserRequest createRandom() {
        final String randomEmail;
        if (randomBoolean()) {
            randomEmail = randomEmail();
        } else {
            randomEmail = null;
        }

        return new UpdateUserRequestBuilder()
                .nickname(getRandomAlphaNumericString(8))
                .email(randomEmail)
                .build();
    }
}

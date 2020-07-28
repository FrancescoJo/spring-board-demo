/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.user.dto;

import com.github.fj.board.endpoint.v1.user.request.CreateUserRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.randomBoolean;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEmail;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
public final class CreateUserRequestBuilder {
    private String nickname = "";
    private String email = null;
    private String invitedBy = null;

    public CreateUserRequestBuilder() {
    }

    public CreateUserRequestBuilder(final CreateUserRequest src) {
        this.nickname = src.getNickname();
        this.email = src.getEmail();
        this.invitedBy = src.getInvitedBy();
    }

    public CreateUserRequestBuilder nickname(final @Nonnull String value) {
        this.nickname = value;
        return this;
    }

    public CreateUserRequestBuilder email(final @Nullable String value) {
        this.email = value;
        return this;
    }

    public CreateUserRequestBuilder invitedBy(final @Nullable String value) {
        this.invitedBy = value;
        return this;
    }

    public CreateUserRequest build() {
        return new CreateUserRequest(
                /* nickname */  nickname,
                /* email */     email,
                /* invitedBy */ invitedBy
        );
    }

    public static CreateUserRequest createRandom() {
        final String randomEmail;
        if (randomBoolean()) {
            randomEmail = randomEmail();
        } else {
            randomEmail = null;
        }

        return new CreateUserRequestBuilder()
                .nickname(getRandomAlphaNumericString(8))
                .email(randomEmail)
                .invitedBy(null)
                .build();
    }
}

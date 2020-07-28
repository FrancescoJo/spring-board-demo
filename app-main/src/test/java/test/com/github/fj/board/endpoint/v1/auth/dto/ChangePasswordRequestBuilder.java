/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.auth.dto;

import com.github.fj.board.endpoint.v1.auth.request.ChangePasswordRequest;
import com.github.fj.lib.security.HashUtilsKt;
import com.github.fj.lib.text.StringUtilsKt;
import com.github.fj.lib.util.ProtectedProperty;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Jul - 2020
 */
public final class ChangePasswordRequestBuilder {
    private String oldPassword = "";
    private String newPassword = "";

    public ChangePasswordRequestBuilder() {
    }

    public ChangePasswordRequestBuilder(final ChangePasswordRequest src) {
        this.oldPassword = src.getOldPassword().getValue();
        this.newPassword = src.getNewPassword().getValue();
    }

    public ChangePasswordRequestBuilder oldPassword(final String value) {
        this.oldPassword = value;
        return this;
    }

    public ChangePasswordRequestBuilder newPassword(final String value) {
        this.newPassword = value;
        return this;
    }

    public ChangePasswordRequest build() {
        final String sha1EncodedOldPw = StringUtilsKt.toHexString(HashUtilsKt.toSha1Bytes(oldPassword));
        final String sha1EncodedNewPw = StringUtilsKt.toHexString(HashUtilsKt.toSha1Bytes(newPassword));

        return new ChangePasswordRequest(
                new ProtectedProperty<>(sha1EncodedOldPw), new ProtectedProperty<>(sha1EncodedNewPw)
        );
    }

    public static ChangePasswordRequest createRandom() {
        return new ChangePasswordRequestBuilder()
                .oldPassword(getRandomAlphaNumericString(16))
                .newPassword(getRandomAlphaNumericString(16))
                .build();
    }
}

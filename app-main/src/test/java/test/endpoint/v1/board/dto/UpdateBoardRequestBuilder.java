/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.endpoint.v1.board.dto;

import com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequest;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
public class UpdateBoardRequestBuilder {
    private String name = "";
    private String description = "";

    public UpdateBoardRequestBuilder() {
    }

    public UpdateBoardRequestBuilder(final @Nonnull UpdateBoardRequest src) {
        this.name = src.getName();
        this.description = src.getDescription();
    }

    public UpdateBoardRequest build() {
        return new UpdateBoardRequest(
                /* name */        name,
                /* description */ description
        );
    }

    public UpdateBoardRequestBuilder name(final @Nonnull String value) {
        this.name = value;
        return this;
    }

    public UpdateBoardRequestBuilder description(final @Nonnull String value) {
        this.description = value;
        return this;
    }

    public static UpdateBoardRequest createRandom() {
        return new UpdateBoardRequestBuilder()
                .name("name: " + getRandomAlphaNumericString(12))
                .description("desc: " + getRandomAlphaNumericString(24))
                .build();
    }
}

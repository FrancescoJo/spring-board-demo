/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.endpoint.v1.board.dto;

import com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequest;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
public class CreateBoardRequestBuilder {
    private String key = "";
    private String name = "";
    private String description = "";

    public CreateBoardRequestBuilder() {
    }

    public CreateBoardRequestBuilder(final @Nonnull CreateBoardRequest src) {
        this.key = src.getKey();
        this.name = src.getName();
        this.description = src.getDescription();
    }

    public CreateBoardRequest build() {
        return new CreateBoardRequest(
                /* key */         key,
                /* name */        name,
                /* description */ description
        );
    }

    public CreateBoardRequestBuilder key(final @Nonnull String value) {
        this.key = value;
        return this;
    }

    public CreateBoardRequestBuilder name(final @Nonnull String value) {
        this.name = value;
        return this;
    }

    public CreateBoardRequestBuilder description(final @Nonnull String value) {
        this.description = value;
        return this;
    }

    public static CreateBoardRequest createRandom() {
        return new CreateBoardRequestBuilder()
                .key(getRandomAlphaNumericString(8))
                .name("name: " + getRandomAlphaNumericString(12))
                .description("desc: " + getRandomAlphaNumericString(24))
                .build();
    }
}

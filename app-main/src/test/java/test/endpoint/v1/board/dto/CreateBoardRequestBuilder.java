/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.endpoint.v1.board.dto;

import com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequest;
import com.github.fj.board.persistence.model.board.Access;
import com.github.fj.board.persistence.model.board.Mode;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
public class CreateBoardRequestBuilder {
    private String key = "";
    private String name = "";
    private String description = "";
    private Access access = Access.PUBLIC;
    private Mode mode = Mode.FREE_STYLE;

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
                /* description */ description,
                /* access */      access,
                /* mode */        mode
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

    public CreateBoardRequestBuilder access(final @Nonnull Access value) {
        this.access = value;
        return this;
    }

    public CreateBoardRequestBuilder mode(final @Nonnull Mode value) {
        this.mode = value;
        return this;
    }

    public static CreateBoardRequest createRandom() {
        return new CreateBoardRequestBuilder()
                .key(getRandomAlphaNumericString(8))
                .name("name: " + getRandomAlphaNumericString(12))
                .description("desc: " + getRandomAlphaNumericString(24))
                .access(randomEnumConst(Access.class))
                .mode(randomEnumConst(Mode.class))
                .build();
    }
}

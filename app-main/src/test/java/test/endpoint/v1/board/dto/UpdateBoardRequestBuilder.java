/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.endpoint.v1.board.dto;

import com.github.fj.board.endpoint.v1.board.dto.UpdateBoardRequest;
import com.github.fj.board.persistence.model.board.Access;
import com.github.fj.board.persistence.model.board.Mode;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
public class UpdateBoardRequestBuilder {
    private String name = "";
    private String description = "";
    private Access access = Access.PUBLIC;
    private Mode mode = Mode.FREE_STYLE;

    public UpdateBoardRequestBuilder() {
    }

    public UpdateBoardRequestBuilder(final @Nonnull UpdateBoardRequest src) {
        this.name = src.getName();
        this.description = src.getDescription();
    }

    public UpdateBoardRequest build() {
        return new UpdateBoardRequest(
                /* name */        name,
                /* description */ description,
                /* access */      access,
                /* mode */        mode
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

    public UpdateBoardRequestBuilder access(final @Nonnull Access value) {
        this.access = value;
        return this;
    }

    public UpdateBoardRequestBuilder mode(final @Nonnull Mode value) {
        this.mode = value;
        return this;
    }

    public static UpdateBoardRequest createRandom() {
        return new UpdateBoardRequestBuilder()
                .name("name: " + getRandomAlphaNumericString(12))
                .description("desc: " + getRandomAlphaNumericString(24))
                .access(randomEnumConst(Access.class))
                .mode(randomEnumConst(Mode.class))
                .build();
    }
}

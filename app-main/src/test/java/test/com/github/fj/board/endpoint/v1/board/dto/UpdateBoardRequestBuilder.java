/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.board.dto;

import com.github.fj.board.endpoint.v1.board.request.UpdateBoardRequest;
import com.github.fj.board.persistence.model.board.BoardAccess;
import com.github.fj.board.persistence.model.board.BoardMode;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
public final class UpdateBoardRequestBuilder {
    private String name = "";
    private String description = "";
    private BoardAccess access = BoardAccess.PUBLIC;
    private BoardMode mode = BoardMode.FREE_STYLE;

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

    public UpdateBoardRequestBuilder access(final @Nonnull BoardAccess value) {
        this.access = value;
        return this;
    }

    public UpdateBoardRequestBuilder mode(final @Nonnull BoardMode value) {
        this.mode = value;
        return this;
    }

    public static UpdateBoardRequest createRandom() {
        return new UpdateBoardRequestBuilder()
                .name("name: " + getRandomAlphaNumericString(12))
                .description("desc: " + getRandomAlphaNumericString(24))
                .access(randomEnumConst(BoardAccess.class))
                .mode(randomEnumConst(BoardMode.class))
                .build();
    }
}

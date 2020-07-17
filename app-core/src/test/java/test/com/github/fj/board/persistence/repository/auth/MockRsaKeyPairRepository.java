/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.repository.auth;

import com.github.fj.board.persistence.entity.auth.RsaKeyPair;
import com.github.fj.board.persistence.repository.auth.RsaKeyPairRepository;
import com.github.fj.board.persistence.repository.auth.RsaKeyPairRepositoryExtension;
import test.com.github.fj.board.persistence.MockJpaRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
public class MockRsaKeyPairRepository extends MockJpaRepository<RsaKeyPair, UUID>
        implements RsaKeyPairRepository, RsaKeyPairRepositoryExtension {
    @Nullable
    @Override
    public RsaKeyPair findLatestOneYoungerThan(final @Nonnull LocalDateTime timeLimit) {
        final List<RsaKeyPair> allKeyPairs = super.findAll();

        allKeyPairs.sort(Comparator.comparing(RsaKeyPair::getIssuedAt));

        for (final RsaKeyPair keyPair : allKeyPairs) {
            if (keyPair.isEnabled() && keyPair.getIssuedAt().compareTo(timeLimit) > 0) {
                return keyPair;
            }
        }

        return null;
    }
}

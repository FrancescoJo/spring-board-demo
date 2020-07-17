/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.appconfig.security;

import com.github.fj.board.appconfig.security.auth.RsaKeyPairManager;
import com.github.fj.board.component.property.AppAuthProperties;
import com.github.fj.board.persistence.repository.auth.RsaKeyPairRepository;
import test.com.github.fj.board.component.property.MockAppAuthProperties;
import test.com.github.fj.board.persistence.repository.auth.MockRsaKeyPairRepository;

import javax.annotation.Nonnull;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Nov - 2018
 */
public class MockRsaKeyPairManager extends RsaKeyPairManager {
    public MockRsaKeyPairManager(final long rsaKeyAliveHours) {
        this(rsaKeyAliveHours, new MockAppAuthProperties(), new MockRsaKeyPairRepository());
    }

    public MockRsaKeyPairManager(
            final long rsaKeyAliveHours,
            final @Nonnull AppAuthProperties authProps,
            final @Nonnull RsaKeyPairRepository keyPairRepo
    ) {
        super(authProps, keyPairRepo);

        if (authProps instanceof MockAppAuthProperties) {
            final MockAppAuthProperties mock = (MockAppAuthProperties) authProps;
            mock.setRsaKeyAliveHours(rsaKeyAliveHours);
        }
    }
}

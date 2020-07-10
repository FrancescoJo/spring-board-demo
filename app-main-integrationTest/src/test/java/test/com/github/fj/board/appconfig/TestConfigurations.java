/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.appconfig;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.test.context.TestConfiguration;

import java.security.Security;

/**
 * @since 07 - Jul - 2020
 */
@TestConfiguration
public class TestConfigurations {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}

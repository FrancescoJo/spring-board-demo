/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.auth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fj.board.endpoint.v1.auth.request.AuthenticationRequest;
import com.github.fj.board.persistence.model.auth.PlatformType;
import com.github.fj.lib.security.HashUtilsKt;
import com.github.fj.lib.text.StringUtilsKt;
import com.github.fj.lib.util.ProtectedProperty;
import de.skuzzle.semantic.Version;

import java.util.HashMap;
import java.util.Map;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveInt;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jul - 2020
 */
public final class AuthenticationRequestBuilder {
    private String loginName;
    private String password;
    private PlatformType platformType;
    private String platformVersion;
    private String appVersion;

    public AuthenticationRequestBuilder() {
    }

    public AuthenticationRequestBuilder(final AuthenticationRequest src) {
        this.loginName = src.getLoginName();
        this.password = src.getPassword().getValue();
        this.platformType = src.getPlatformType();
        this.platformVersion = src.getPlatformVersion();
        this.appVersion = src.getAppVersion().toString();
    }

    public AuthenticationRequestBuilder loginName(final String loginName) {
        this.loginName = loginName;
        return this;
    }

    public AuthenticationRequestBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public AuthenticationRequestBuilder passwordHashed(final String password) {
        this.password = StringUtilsKt.toHexString(HashUtilsKt.toSha1Bytes(password));
        return this;
    }

    public AuthenticationRequestBuilder platformType(final PlatformType platformType) {
        this.platformType = platformType;
        return this;
    }

    public AuthenticationRequestBuilder platformVersion(final String platformVersion) {
        this.platformVersion = platformVersion;
        return this;
    }

    public AuthenticationRequestBuilder appVersion(final String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public AuthenticationRequest build() {
        return new AuthenticationRequest(
                /* loginName */       loginName,
                /* password */        new ProtectedProperty<>(password),
                /* platformType */    platformType,
                /* platformVersion */ platformVersion,
                /* appVersion */      Version.parseVersion(appVersion)
        );
    }

    public String buildAsJsonBy(final ObjectMapper jsonMapper) throws Exception {
        final Map<String, Object> rawRequest = new HashMap<>();

        rawRequest.put("loginName", loginName);
        rawRequest.put("password", password);
        rawRequest.put("platformType", platformType);
        rawRequest.put("platformVersion", platformVersion);
        rawRequest.put("appVersion", appVersion);

        return jsonMapper.writeValueAsString(rawRequest);
    }

    public static AuthenticationRequest createRandom() {
        return new AuthenticationRequestBuilder()
                .loginName(getRandomAlphaNumericString(
                        getRandomPositiveInt(AuthenticationRequest.LOGIN_NAME_SIZE_MIN, AuthenticationRequest.LOGIN_NAME_SIZE_MAX)
                ))
                .password(getRandomAlphaNumericString((int) AuthenticationRequest.PASSWORD_SIZE))
                .platformType(randomEnumConst(PlatformType.class))
                .platformVersion(getRandomAlphaNumericString(64))
                .appVersion(Version.ZERO.toString())
                .build();
    }
}

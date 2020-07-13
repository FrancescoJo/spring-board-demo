/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.endpoint.v1.auth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fj.board.endpoint.v1.auth.dto.SignUpRequest;
import com.github.fj.board.persistence.model.auth.PlatformType;
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
public final class SignUpRequestBuilder {
    private String loginName;
    private String password;
    private PlatformType platformType;
    private String platformVersion;
    private String appVersion;

    public SignUpRequestBuilder() {
    }

    public SignUpRequestBuilder(final SignUpRequest src) {
        this.loginName = src.getLoginName();
        this.password = src.getPassword().getValue();
        this.platformType = src.getPlatformType();
        this.platformVersion = src.getPlatformVersion();
        this.appVersion = src.getAppVersion().toString();
    }

    public SignUpRequestBuilder loginName(final String loginName) {
        this.loginName = loginName;
        return this;
    }

    public SignUpRequestBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public SignUpRequestBuilder platformType(final PlatformType platformType) {
        this.platformType = platformType;
        return this;
    }

    public SignUpRequestBuilder platformVersion(final String platformVersion) {
        this.platformVersion = platformVersion;
        return this;
    }

    public SignUpRequestBuilder appVersion(final String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public SignUpRequest build() {
        return new SignUpRequest(
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

    public static SignUpRequest createRandom() {
        return new SignUpRequestBuilder()
                .loginName(getRandomAlphaNumericString(
                        getRandomPositiveInt(SignUpRequest.LOGIN_NAME_SIZE_MIN, SignUpRequest.LOGIN_NAME_SIZE_MAX)
                ))
                .password(getRandomAlphaNumericString((int) SignUpRequest.PASSWORD_SIZE))
                .platformType(randomEnumConst(PlatformType.class))
                .platformVersion(getRandomAlphaNumericString(64))
                .appVersion(Version.ZERO.toString())
                .build();
    }
}

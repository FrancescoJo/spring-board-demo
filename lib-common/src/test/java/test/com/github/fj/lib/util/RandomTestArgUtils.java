/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package test.com.github.fj.lib.util;

import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveInt;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2018
 */
public class RandomTestArgUtils {
    public static <T extends Enum<T>> T randomEnumConst(final Class<T> klass) {
        return randomEnumConst(klass, null);
    }

    public static <T extends Enum<T>> T randomEnumConst(final Class<T> klass,
                                                        final Function<T, Boolean> acceptFunction) {
        final T[] constants = klass.getEnumConstants();

        T randomValue;
        boolean acceptFuncResult;
        do {
            final int randomIndex = new Random().nextInt(constants.length);
            randomValue = constants[randomIndex];
            if (acceptFunction == null) {
                acceptFuncResult = true;
            } else {
                acceptFuncResult = acceptFunction.apply(randomValue);
            }
        } while (!acceptFuncResult || "UNDEFINED".equals(randomValue.name()));

        return randomValue;
    }

    public static Locale randomLocale() {
        final Locale[] locales = Locale.getAvailableLocales();
        Locale l;
        if (locales.length == 1) {
            l = locales[0];
            if (l.getCountry().isEmpty() || l.getLanguage().isEmpty()) {
                throw new UnsupportedOperationException("This system supports no locale to perform tests");
            }
        } else {
            do {
                l = locales[getRandomPositiveInt(0, locales.length)];
            } while (l.getCountry().isEmpty() || l.getLanguage().isEmpty());
        }

        return l;
    }

    public static String randomEmail() {
        final int nameLength = getRandomPositiveInt(1, 16);
        return getRandomAlphaNumericString(nameLength) + "@" + randomElementOf(DEFAULT_DOMAINS);
    }

    public static String randomUri() {
        final String scheme = randomElementOf(DEFAULT_SCHEMES);
        final String host = randomElementOf(DEFAULT_DOMAINS);
        final StringBuilder pathSegs = new StringBuilder("/");
        final int iterations = getRandomPositiveInt(1, 4);

        for (int i = 0; i < iterations; i++) {
            pathSegs.append(getRandomAlphaNumericString(8));
            if (i < iterations - 1) {
                pathSegs.append("/");
            }
        }

        return scheme + "://" + host + pathSegs.toString();
    }

    public static String randomMimeType() {
        return randomElementOf(DEFAULT_MIME_TYPES);
    }

    private static <T> T randomElementOf(final T[] array) {
        return array[getRandomPositiveInt(0, array.length)];
    }

    private static final String[] DEFAULT_SCHEMES = new String[]{
            "http", "https", "ftp"
    };
    private static final String[] DEFAULT_DOMAINS = new String[]{
            "company.com", "organisation.org", "institute.edu", "government.gov", "network.net"
    };
    private static final String[] DEFAULT_MIME_TYPES = new String[]{
            "image/bmp", "image/gif", "image/jpeg", "image/png", "image/webp", "image/svg+xml",
            "audio/aac", "audio/mpeg", "audio/ogg", "audio/wav", "audio/webm",
            "video/x-msvideo", "video/mpeg", "video/ogg", "video/webm",
    };
}

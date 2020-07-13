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
}

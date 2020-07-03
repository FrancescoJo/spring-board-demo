/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.lang;

import javax.annotation.Nullable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 7 - Apr - 2017
 */
public class ToStringUtils {
    private ToStringUtils() { }

    /**
     * Convenient implementation of {@link java.lang.Object#toString()} for debugging purpose.
     * Dumps all fields of given object via reflection. Since this method <i>accesses all fields</i>
     * of given object and to avoid risks of deadlock, it is not recommended to inspect objects
     * those have internal field locking mechanism.
     * <p>
     * As description denotes, this method is strictly designed for debugging purpose.
     * Since your object <i>may</i> includes sensitive information that must not be dumped to
     * outside world, this method will return default {@link java.lang.Object#toString()}
     * implementation in non-verbose logging mode.
     * </p>
     * <p>
     * This implementation skips dumping transient fields.
     * </p>
     */
    public static String toString(final @Nullable Object obj) {
        return toString(obj, false);
    }

    /**
     * Convenient implementation of {@link java.lang.Object#toString()} for debugging purpose.
     * Dumps all fields of given object via reflection. Since this method <i>accesses all fields</i>
     * of given object and to avoid risks of deadlock, it is not recommended to inspect objects
     * those have internal field locking mechanism.
     * <p>
     * As description denotes, this method is strictly designed for debugging purpose.
     * Since your object <i>may</i> includes sensitive information that must not be dumped to
     * outside world, this method will return default {@link java.lang.Object#toString()}
     * implementation in non-verbose logging mode.
     * </p>
     */
    @SuppressWarnings("SameParameterValue")
    public static String toString(final @Nullable Object obj, final boolean printTransients) {
        return null == obj ? ToStringReflector.NULL : ToStringReflector.toString(obj, printTransients);
    }

    /**
     * @author Francesco Jo(nimbusob@gmail.com)
     * @since 7 - Apr - 2017
     */
    static final class ToStringReflector {
        static final String NULL = "null";

        private static final int INDENT_SIZE = 2;

        private ToStringReflector() { }

        static String toString(final Object obj, final boolean printTransients) {
            return toStringInternal(new StringBuilder(), obj, printTransients);
        }

        private static String toStringInternal(
                final StringBuilder sb,
                final Object obj,
                final boolean printTransients
        ) {
            sb.append(obj.getClass().getCanonicalName()).append('@')
                    .append(obj.hashCode()).append("{\n");

            final Field[] fields = obj.getClass().getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            final String indents = indents(INDENT_SIZE);
            for (final Field field : fields) {
                // Skips transients by settings, and statics.
                if (Modifier.isTransient(field.getModifiers()) && !printTransients) {
                    continue;
                } else if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                try {
                    sb.append(indents).append(field.getName()).append(": ");
                    final Object value = field.get(obj);
                    if (null == value) {
                        sb.append(NULL);
                    } else {
                        final Class<?> valueType = value.getClass();
                        if (valueType.isArray()) {
                            sb.append('[');
                            toStringArray(sb, value);
                            sb.append(']');
                        } else {
                            sb.append(value);
                        }
                    }
                    sb.append('\n');
                } catch (final IllegalAccessException ignore) {
                    /*
                     * This can't be happen. Would get a SecurityException instead.
                     * We don't want to let app crash because of dumping a debug info.
                     */
                    return "";
                }
            }

            sb.append('}');
            return sb.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private static String indents(final int size) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append(' ');
            }
            return sb.toString();
        }

        private static void toStringArray(final StringBuilder sb, final Object array) {
            final int size = Array.getLength(array);
            for (int i = 0; i < size; i++) {
                sb.append(Array.get(array, i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
        }
    }
}

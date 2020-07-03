/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.fj.lib.function;

import java.util.function.Consumer;

/**
 * A dirty java type system hack to make it possible to throw a checked exception in
 * lambda expression.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @see <a href="https://stackoverflow.com/questions/31316581/a-peculiar-feature-of-exception-type-inference-in-java-8">
 * A peculiar feature of exception type inference in Java 8</a>
 * @since 09 - Nov - 2018
 */
@FunctionalInterface
public interface ExceptionThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;

    static <T, E extends Exception> Consumer<T> exceptional(
            final ExceptionThrowingConsumer<T, Exception> throwingConsumer
    ) throws E {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (final Exception e) {
                sneakyThrow(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> void sneakyThrow(final Throwable t) throws T {
        throw (T) t;
    }
}

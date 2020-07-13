/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
@SuppressWarnings("NullableProblems")
public class MockJpaRepository<T, ID> implements JpaRepository<T, ID> {
    private final Map<ID, T> store = new ConcurrentHashMap<>();

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<T> findAll(final Sort sort) {
        throw new UnsupportedOperationException("Mock is not implemented");
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        throw new UnsupportedOperationException("Mock is not implemented");
    }

    @Override
    public List<T> findAllById(final Iterable<ID> ids) {
        final List<T> values = new ArrayList<>();

        ids.forEach(id -> findById(id).ifPresent(values::add));

        return values;
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void deleteById(final ID id) {
        store.remove(id);
    }

    @Override
    public void delete(final T entity) {
        final ID id = getIdFrom(entity);
        if (id != null) {
            store.remove(id);
            return;
        }

        store.values().remove(entity);
    }

    @Override
    public void deleteAll(final Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    /**
     * Unlike real JPA repository, this method will not work if entity have a null or empty {@link Id}.
     */
    @Override
    public <S extends T> S save(final S entity) {
        final ID id = getIdFrom(entity);
        if (id == null) {
            throw new IllegalArgumentException(
                    "Id is null or " + Id.class + " annotated field was found in " + entity.getClass()
            );
        }

        @SuppressWarnings("unchecked") final S result = (S) store.put(id, entity);
        return result;
    }

    @Override
    public <S extends T> List<S> saveAll(final Iterable<S> entities) {
        final List<S> results = new ArrayList<>();

        entities.forEach(entity -> results.add(this.save(entity)));

        return results;
    }

    @Override
    public Optional<T> findById(final ID id) {
        final T value = store.get(id);

        if (value == null) {
            return Optional.empty();
        } else {
            return Optional.of(value);
        }
    }

    @Override
    public boolean existsById(final ID id) {
        return store.containsKey(id);
    }

    @Override
    public void flush() { /* no-op */ }

    @Override
    public <S extends T> S saveAndFlush(final S entity) {
        return save(entity);
    }

    @Override
    public void deleteInBatch(final Iterable<T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public T getOne(final ID id) {
        return findById(id).get();
    }

    @Override
    public <S extends T> Optional<S> findOne(final Example<S> example) {
        final List<S> maybe = findAll(example);
        if (maybe.isEmpty()) {
            return Optional.empty();
        }

        if (maybe.size() == 1) {
            return Optional.of(maybe.get(0));
        }

        throw new IncorrectResultSizeDataAccessException(1, maybe.size());
    }

    @Override
    public <S extends T> List<S> findAll(final Example<S> example) {
        return findAllExamples(example, findAll());
    }

    @Override
    public <S extends T> List<S> findAll(final Example<S> example, Sort sort) {
        return findAllExamples(example, findAll(sort));
    }

    @Override
    public <S extends T> Page<S> findAll(final Example<S> example, Pageable pageable) {
        return new PageImpl<>(findAllExamples(example, findAll(pageable)));
    }

    @Override
    public <S extends T> long count(final @Nonnull Example<S> example) {
        return findAll(example).size();
    }

    @Override
    public <S extends T> boolean exists(final @Nonnull Example<S> example) {
        return !findAll(example).isEmpty();
    }

    private <S extends T> List<S> findAllExamples(
            final @Nonnull Example<S> example,
            final @Nonnull Iterable<T> entities
    ) {
        final List<S> list = new ArrayList<>();

        for (final T entity : entities) {
            if (entity.equals(example)) {
                @SuppressWarnings("unchecked") final S castedEntity = (S) entity;
                list.add(castedEntity);
            }
        }

        return list;
    }

    private <S extends T> ID getIdFrom(final S entity) {
        final Method[] methods = entity.getClass().getDeclaredMethods();
        AccessibleObject.setAccessible(methods, true);

        final Method idMethod = findIdAnnotatedMember(methods);
        if (idMethod != null) {
            try {
                @SuppressWarnings("unchecked") final ID idValue = (ID) idMethod.invoke(entity);
                return idValue;
            } catch (InvocationTargetException | IllegalAccessException ex) {
                System.err.println(Id.class +
                        " annotated method was found but it was not a simple getter: " + idMethod);
            }
        }

        final Field[] fields = entity.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        final Field idField = findIdAnnotatedMember(fields);
        if (idField != null) {
            try {
                @SuppressWarnings("unchecked") final ID idValue = (ID) idField.get(entity);
                return idValue;
            } catch (IllegalAccessException ex) {
                System.err.println(Id.class +
                        " annotated method was found but unable to access to " + idField);
            }
        }

        System.err.println("No " + Id.class + " annotated field was found in " + entity.getClass());
        return null;
    }

    private static <M extends AccessibleObject> M findIdAnnotatedMember(final AccessibleObject[] members) {
        for (final AccessibleObject ao : members) {
            final Annotation[] annotations = ao.getDeclaredAnnotations();
            if (annotations.length == 0) {
                continue;
            }

            for (final Annotation an : annotations) {
                if (an.annotationType() == Id.class) {
                    @SuppressWarnings("unchecked") final M result = (M) ao;
                    return result;
                }
            }
        }

        return null;
    }
}

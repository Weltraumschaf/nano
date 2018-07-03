package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.Require;
import de.weltraumschaf.nano.api.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Injects all {@link Require required services}  on a service which need these dependencies.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class Injector {
    private static final Logger LOG = LoggerFactory.getLogger(Injector.class);
    private final ServiceRegistry services;

    /**
     * Dedicated constructor.
     *
     * @param services not {@code null}
     */
    Injector(final ServiceRegistry services) {
        super();
        this.services = Validate.notNull(services, "services");
    }

    /**
     * Injects all {@link Require required services} on the target service.
     *
     * @param target not {@code null}
     */
    void injectRequired(final Service target) {
        Validate.notNull(target, "target");
        LOG.debug("Inject required fields to {} ...", target.getClass().getCanonicalName());
        final Collection<Field> found = findRequiredFields(target.getClass());

        if (found.isEmpty()) {
            LOG.debug("No required fields found for '{}'.", target.getClass().getCanonicalName());
            return;
        }

        found.forEach(f -> {
            final boolean accessible = f.isAccessible();

            if (!accessible) {
                f.setAccessible(true);
            }

            injectFields(target, f);

            if (!accessible) {
                f.setAccessible(false);
            }
        });
    }

    /**
     * Finds all required fields.
     * <p>
     * This method goes through the whole class hierarchy to find required fields
     * </p>
     *
     * @param type may be {@code null}
     * @return never {@code null}
     */
    Collection<Field> findRequiredFields(final Class<?> type) {
        if (null == type) {
            return Collections.emptyList();
        }

        LOG.debug("Find required fields for '{}'", type.getCanonicalName());

        final List<Field> found = Arrays.stream(type.getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(Require.class))
            .collect(Collectors.toList());

        if (null != type.getSuperclass()) {
            found.addAll(findRequiredFields(type.getSuperclass()));
        }

        return found;
    }

    private void injectFields(final Service target, final Field f) {
        final Class<?> requiredType = f.getType();

        LOG.debug("Required filed '{}' wants a service.", f);
        final Optional<?> required = services.findOne(requiredType);

        if (!required.isPresent()) {
            throw new IllegalStateException(
                String.format("Can't find required service '%s' for '%s'!",
                    requiredType.getCanonicalName(), target.getClass().getCanonicalName()));
        }

        try {
            LOG.debug("Inject required '{}' on '{}'.",
                required.get().getClass().getCanonicalName(),
                target.getClass().getCanonicalName());
            f.set(target, required.get());
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}

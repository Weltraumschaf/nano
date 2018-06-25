package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.Require;
import de.weltraumschaf.nano.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Injects all {@link Require required services}  on a service which need these dependencies.
 *
 * @since 1.0.0
 */
final class Injector {
    private static Logger LOG = LoggerFactory.getLogger(Injector.class);
    private final Services services;

    /**
     * Dedicated constructor.
     *
     * @param services not {@code null}
     */
    Injector(final Services services) {
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
        findRequiredFields(target).forEach(f -> {
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
     *
     * @param target may be {@code null}
     * @return never {@code null}
     */
    Collection<Field> findRequiredFields(final Service target) {
        if (null == target) {
            return Collections.emptyList();
        }

        LOG.debug("Find required fields for '{}'", target.getClass().getCanonicalName());
        return Arrays.stream(target.getClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(Require.class))
            .collect(Collectors.toList());
    }

    private void injectFields(final Service target, final Field f) {
        final Class<?> requiredType = f.getType();

        LOG.debug("Required filed '{}' wants a service.", f);
        final Optional<?> required = services.findService(requiredType);

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

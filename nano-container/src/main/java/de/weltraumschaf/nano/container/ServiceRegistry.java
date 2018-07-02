package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.AutoStartingService;
import de.weltraumschaf.nano.api.service.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Holds all services for a running container.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class ServiceRegistry {
    private final Collection<Service> services = new LinkedList<>();

    /**
     * Register a single service instance.
     *
     * @param service not {@code null}
     */
    void register(final Service service) {
        services.add(Validate.notNull(service, "service"));
    }

    /**
     * Finds a service of given type.
     *
     * @param type may be {@code null}
     * @return never {@code null}
     */
    Optional<Service> findOne(final Class<?> type) {
        if (null == type) {
            return Optional.empty();
        }

        return services.stream()
            .filter(s -> type.isAssignableFrom(s.getClass()))
            .findFirst();
    }

    /**
     * Get all registered service instances.
     *
     * @return never {@code null}, unmodifiable
     */
    Collection<Service> findAll() {
        return Collections.unmodifiableCollection(services);
    }

    /**
     * Get all registered auto starting service instances.
     *
     * @return never {@code null}, unmodifiable
     */
    Collection<AutoStartingService> findAutoStarting() {
        return Collections.unmodifiableCollection(services.stream()
            .filter(s -> s instanceof AutoStartingService)
            .map(s -> (AutoStartingService) s)
            .collect(Collectors.toList()));
    }

    /**
     * Number of registered services.
     *
     * @return not negative
     */
    int size() {
        return services.size();
    }
}

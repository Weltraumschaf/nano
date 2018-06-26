package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.AutoStartingService;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Delegates the life cycle to all contained services.
 *
 * @since 1.0.0
 */
final class Services {
    private static Logger LOG = LoggerFactory.getLogger(Services.class);
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final Collection<Service> services;

    /**
     * Dedicated constructor.
     *
     * @param services not {@code null}, defensive copied
     */
    Services(final Collection<Service> services) {
        super();
        this.services = new ArrayList<>(Validate.notNull(services, "services"));
    }

    /**
     * Start all services.
     * <p>
     * This means:
     * </p>
     * <ol>
     * <li>inject all required dependent services</li>
     * <li>activate all services</li>
     * <li>start services marked as {@link AutoStartingService}</li>
     * </ol>
     *
     * @param messages not {@code null}
     */
    void start(final MessageBus messages) {
        Validate.notNull(messages, "getMessages");
        injectRequiredServices();
        activate(messages);
        autoStart();
    }

    /**
     * Stops all services which are marked as {@link AutoStartingService auto starting}.
     */
    void stop() {
        LOG.debug("Auto stop services ...", services.size());
        final int count = services.stream()
            .filter(s -> s instanceof AutoStartingService)
            .map(s -> (AutoStartingService) s)
            .mapToInt(s -> {
                s.stop();
                return 1;
            }).sum();
        LOG.debug("{} services auto stopped.");
    }

    /**
     * Deactivates all services.
     */
    void deactivate() {
        LOG.debug("Deactivating {} services ...", services.size());
        services.forEach(Service::deactivate);
        LOG.debug("All services deactivated.");
    }

    /**
     * Finds a service of given type.
     *
     * @param type may be {@code null}
     * @return never {@code null}
     */
    Optional<Service> findService(final Class<?> type) {
        if (null == type) {
            return Optional.empty();
        }

        return services.stream()
            .filter(s -> type.isAssignableFrom(s.getClass()))
            .findFirst();
    }

    private void injectRequiredServices() {
        final Injector injector = new Injector(this);
        services.forEach(injector::injectRequired);
    }

    private void activate(final MessageBus messages) {
        LOG.debug("Activating {} services ...", services.size());
        services.forEach(service -> service.activate(new DefaultServiceContext(messages)));
        LOG.debug("All services activated.");
    }

    private void autoStart() {
        LOG.debug("Auto start services ...");
        final int count = services.stream()
            .filter(s -> s instanceof AutoStartingService)
            .map(s -> (AutoStartingService) s)
            .mapToInt(s -> {
                pool.execute(s::start);
                return 1;
            }).sum();
        LOG.debug("{} services auto started.", count);
    }

}

package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.AutoStartingService;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Delegates the life cycle to all contained services.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class Services {
    private static final Logger LOG = LoggerFactory.getLogger(Services.class);
    private static final int MILLIS_TO_WAIT = 1_000;
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
        autoStop();
        waitUntilAllServicesHasStopped();
        deactivate();
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
                new Thread(s::start).start();
                return 1;
            }).sum();
        LOG.debug("{} services auto started.", count);
    }

    private void autoStop() {
        LOG.debug("Auto stop services ...", services.size());
        final int count = services.stream()
            .filter(s -> s instanceof AutoStartingService)
            .map(s -> (AutoStartingService) s)
            .filter(AutoStartingService::isRunning)
            .mapToInt(s -> {
                s.stop();
                return 1;
            }).sum();
        LOG.debug("{} services auto stopped.", count);
    }

    private void waitUntilAllServicesHasStopped() {
        while (true) {
            try {
                LOG.debug("Wait for stopping service ...");

                final int count = services.stream()
                    .filter(s -> s instanceof AutoStartingService)
                    .map(s -> (AutoStartingService) s)
                    .filter(s -> !s.hasStopped())
                    .mapToInt(s -> 1)
                    .sum();

                if (count == 0) {
                    LOG.debug("All services stopped.");
                    break;
                }

                LOG.debug("{} services not stopped yet! Waiting {} ms ...", count, MILLIS_TO_WAIT);
                // FIXME BReak out anyway after some maximum wait time.
                Thread.sleep(MILLIS_TO_WAIT);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private void deactivate() {
        LOG.debug("Deactivating {} services ...", services.size());
        services.forEach(Service::deactivate);
        LOG.debug("All services deactivated.");
    }
}

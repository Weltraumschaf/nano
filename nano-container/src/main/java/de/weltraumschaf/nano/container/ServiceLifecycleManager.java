package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.service.AutoStartingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delegates the life cycle to all contained services.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class ServiceLifecycleManager {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceLifecycleManager.class);
    private static final int MILLIS_TO_WAIT = 1_000;
    private static final int MAX_RETRIES = 5;
    private final ServiceRegistry services;

    /**
     * Dedicated constructor.
     *
     * @param services not {@code null}
     */
    ServiceLifecycleManager(final ServiceRegistry services) {
        super();
        this.services = Validate.notNull(services, "services");
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

    private void activate(final MessageBus messages) {
        LOG.debug("Activating {} services ...", services.size());
        services.findAll()
            .forEach(service -> {
                LOG.debug("Activating service: {}.", service);
                service.activate(new DefaultServiceContext(messages));
            });
        LOG.debug("All services activated.");
    }

    private void autoStart() {
        LOG.debug("Auto start services ...");
        final int count = services.findAutoStarting()
            .stream()
            .mapToInt(service -> {
                LOG.debug("Auto start service: {}.", service);
                new Thread(service::start).start();
                return 1;
            }).sum();
        LOG.debug("{} services auto started.", count);
    }

    private void autoStop() {
        LOG.debug("Auto stop services ...", services.size());
        final int count = services.findAutoStarting()
            .stream()
            .filter(AutoStartingService::isRunning)
            .mapToInt(service -> {
                LOG.debug("Auto stop service: {}.", service);
                service.stop();
                return 1;
            }).sum();
        LOG.debug("{} services auto stopped.", count);
    }

    private void waitUntilAllServicesHasStopped() {
        LOG.debug("Wait for stopping service ...");

        try {
            Repeater.of(MILLIS_TO_WAIT, MAX_RETRIES).execute(() -> {
                final int count = services.findAutoStarting()
                    .stream()
                    .filter(service -> !service.hasStopped())
                    .mapToInt(service -> {
                        LOG.debug("Service not yet stopped: {}.", service);
                        return 1;
                    })
                    .sum();

                if (count == 0) {
                    LOG.debug("All services stopped.");
                    return true;
                } else {
                    LOG.debug("{} services not stopped yet! Waiting {} ms ...", count, MILLIS_TO_WAIT);
                    return false;
                }
            });
            LOG.debug("All services stopped.");
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void deactivate() {
        LOG.debug("Deactivating {} services ...", services.size());
        services.findAll().forEach(service -> {
            LOG.debug("Deactivating service: {}.", service);
            service.deactivate();
        });
        LOG.debug("All services deactivated.");
    }
}

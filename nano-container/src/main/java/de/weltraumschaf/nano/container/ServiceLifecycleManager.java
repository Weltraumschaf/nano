package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.service.AutoStartingService;
import de.weltraumschaf.nano.api.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;

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
     * Stops all services.
     * <p>
     * This means:
     * </p>
     * <ol>
     * <li>stop services marked as {@link AutoStartingService}</li>
     * <li>deactivate all services</li>
     * </ol>
     */
    void stop() {
        autoStop();
        waitUntilAllServicesHasStopped();
        deactivate();
    }

    private void activate(final MessageBus messages) {
        LOG.debug("Activating {} services ...", services.size());
        services.findAll()
            .stream()
            .peek(service -> LOG.debug("Activating service: {}.", service))
            .forEach(service -> service.activate(new DefaultServiceContext(messages)));
        LOG.debug("All services activated.");
    }

    private void autoStart() {
        LOG.debug("Auto start services ...");
        final int count = services.findAutoStarting()
            .stream()
            .peek(service -> LOG.debug("Auto start service: {}.", service))
            .mapToInt(service -> {
                new Thread(service::start).start();
                return 1;
            }).sum();
        LOG.debug("{} services auto started.", count);
    }

    private void autoStop() {
        LOG.debug("Auto stop services ...", services.size());
        final int count = services.findAutoStarting()
            .stream()
            .peek(service -> LOG.debug("Auto stop service: {}.", service))
            .mapToInt(service -> {
                service.stop();
                return 1;
            }).sum();
        LOG.debug("{} services auto stopped.", count);
    }

    private void waitUntilAllServicesHasStopped() {
        LOG.debug("Wait for stopping service ...");

        try {
            final Repeater repeater = Repeater.of(MILLIS_TO_WAIT, MAX_RETRIES);
            final boolean successful = repeater.execute(
                new ServiceStopper(services.findAutoStarting()));

            if (successful) {
                LOG.debug("All services stopped.");
            } else {
                LOG.warn("Not all services stopped after {}ms wait and {} retries!",
                    repeater.getWaitMillis(), repeater.getMaxRetries());
            }
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void deactivate() {
        LOG.debug("Deactivating {} services ...", services.size());
        services.findAll()
            .stream()
            .peek(service -> LOG.debug("Deactivating service: {}.", service))
            .forEach(Service::deactivate);
        LOG.debug("All services deactivated.");
    }

    private static final class ServiceStopper implements Callable<Boolean> {
        private static final Logger LOG = LoggerFactory.getLogger(ServiceStopper.class);
        private final Collection<AutoStartingService> services;

        private ServiceStopper(final Collection<AutoStartingService> services) {
            super();
            this.services = Collections.unmodifiableCollection(Validate.notNull(services, "services"));
        }

        @Override
        public Boolean call() {
            final int count = services.stream()
                .filter(service -> !service.hasStopped())
                .peek(service -> LOG.debug("Service not yet stopped (waiting): {}.", service))
                .mapToInt(service -> 1)
                .sum();

            if (count == 0) {
                LOG.debug("All services stopped.");
                return true;
            } else {
                LOG.debug("{} services not stopped yet! Waiting {} ms ...", count, MILLIS_TO_WAIT);
                return false;
            }
        }
    }
}

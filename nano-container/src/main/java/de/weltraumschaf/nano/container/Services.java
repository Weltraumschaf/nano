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
import java.util.concurrent.Callable;

/**
 * Delegates the life cycle to all contained managed.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class Services {
    private static final Logger LOG = LoggerFactory.getLogger(Services.class);
    private static final int MILLIS_TO_WAIT = 1_000;
    private static final int MAX_RETRIES = 5;
    private final Collection<Service> managed;

    /**
     * Dedicated constructor.
     *
     * @param managed not {@code null}, defensive copied
     */
    Services(final Collection<Service> managed) {
        super();
        this.managed = new ArrayList<>(Validate.notNull(managed, "managed"));
    }

    /**
     * Start all managed.
     * <p>
     * This means:
     * </p>
     * <ol>
     * <li>inject all required dependent managed</li>
     * <li>activate all managed</li>
     * <li>start managed marked as {@link AutoStartingService}</li>
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
     * Stops all managed which are marked as {@link AutoStartingService auto starting}.
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

        return managed.stream()
            .filter(s -> type.isAssignableFrom(s.getClass()))
            .findFirst();
    }

    private void injectRequiredServices() {
        final Injector injector = new Injector(this);
        managed.forEach(injector::injectRequired);
    }

    private void activate(final MessageBus messages) {
        LOG.debug("Activating {} managed ...", managed.size());
        managed.forEach(service -> service.activate(new DefaultServiceContext(messages)));
        LOG.debug("All managed activated.");
    }

    private void autoStart() {
        LOG.debug("Auto start managed ...");
        final int count = managed.stream()
            .filter(s -> s instanceof AutoStartingService)
            .map(s -> (AutoStartingService) s)
            .mapToInt(s -> {
                new Thread(s::start).start();
                return 1;
            }).sum();
        LOG.debug("{} managed auto started.", count);
    }

    private void autoStop() {
        LOG.debug("Auto stop managed ...", managed.size());
        final int count = managed.stream()
            .filter(s -> s instanceof AutoStartingService)
            .map(s -> (AutoStartingService) s)
            .filter(AutoStartingService::isRunning)
            .mapToInt(s -> {
                s.stop();
                return 1;
            }).sum();
        LOG.debug("{} managed auto stopped.", count);
    }

    private void waitUntilAllServicesHasStopped() {
        LOG.debug("Wait for stopping service ...");

        try {
            Repeater.of(MILLIS_TO_WAIT, MAX_RETRIES).execute(() -> {
                final int count = managed.stream()
                    .filter(s -> s instanceof AutoStartingService)
                    .map(s -> (AutoStartingService) s)
                    .filter(s -> !s.hasStopped())
                    .mapToInt(s -> 1)
                    .sum();

                if (count == 0) {
                    LOG.debug("All managed stopped.");
                    return true;
                } else {
                    LOG.debug("{} managed not stopped yet! Waiting {} ms ...", count, MILLIS_TO_WAIT);
                    return false;
                }
            });
            LOG.debug("All managed stopped.");
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void deactivate() {
        LOG.debug("Deactivating {} managed ...", managed.size());
        managed.forEach(Service::deactivate);
        LOG.debug("All managed deactivated.");
    }
}

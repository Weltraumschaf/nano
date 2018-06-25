package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.AutoStartingService;
import de.weltraumschaf.nano.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @since 1.0.0
 */
final class Services {
    private static Logger LOG = LoggerFactory.getLogger(Services.class);
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final Collection<Service> services;

    Services(final Collection<Service> services) {
        super();
        this.services = new ArrayList<>(Validate.notNull(services, "services"));
    }

    void activate() {
        LOG.debug("Activating {} services ...", services.size());
        services.forEach(Service::activate);
        LOG.debug("All services activated.");
    }

    void autoStart() {
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

    void autoStop() {
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

    void deactivate() {
        LOG.debug("Deactivating {} services ...", services.size());
        services.forEach(Service::deactivate);
        LOG.debug("All services deactivated.");
    }
}

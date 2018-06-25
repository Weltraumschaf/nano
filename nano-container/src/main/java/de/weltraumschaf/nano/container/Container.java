package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @since 1.0.0
 */
public final class Container {
    private static Logger LOG = LoggerFactory.getLogger(Container.class);

    private volatile boolean running = true;
    private volatile boolean stopped = false;
    private final Collection<Service> services = new ArrayList<>();

    public void start() {
        LOG.info("Container starts...");
        activateServices(findModules());
        LOG.info("Container started.");
        loop();
        stopped = true;
    }


    public void stop() {
        LOG.info("Container is stopping...");
        running = false;

        while (!stopped) {
            try {
                LOG.debug("Wait for stopping ...");
                Thread.sleep(10);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        deactivateServices();
        LOG.info("Container stopped.");
    }

    private Collection<ModuleDescription> findModules() {
        LOG.debug("Find modules...");
        final Collection<ModuleDescription> modules = new ModuleFinder().find();
        LOG.debug("Found {} modules.", modules.size());
        return modules;
    }

    private void activateServices(final Collection<ModuleDescription> modules) {
        LOG.debug("Activate services ...");
        final ServiceActivator activator = new ServiceActivator();
        modules.forEach(module -> services.addAll(activator.activate(module)));
        LOG.debug("All services activated.");
    }

    private void loop() {
        while (running) {
            try {
                LOG.info("Container is running.");
                Thread.sleep(1_000);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
                return;
            }
        }
    }

    private void deactivateServices() {
        LOG.debug("Deactivate all services ...");
        services.forEach(Service::deactivate);
        LOG.debug("All services deactivated.");
    }
}

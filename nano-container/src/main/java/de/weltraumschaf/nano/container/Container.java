package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @since 1.0.0
 */
public final class Container {
    private static Logger LOG = LoggerFactory.getLogger(Container.class);

    private volatile boolean running;
    private volatile boolean stopped;
    private Services services;

    public void start() {
        LOG.info("Container starts...");
        running = true;
        final Collection<ModuleDescription> modules = findModules();
        services = new Services(createServices(modules));
        services.activate();
        services.autoStart();
        LOG.info("Container started.");

        loop();

        stopped = true;
    }


    public void stop() {
        if (!running) {
            throw new IllegalStateException("Container never started!");
        }

        LOG.info("Container is stopping...");
        running = false;

        services.autoStop();

        while (!stopped) {
            try {
                LOG.debug("Wait for stopping ...");
                Thread.sleep(10);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        services.deactivate();
        LOG.info("Container stopped.");
    }

    private Collection<ModuleDescription> findModules() {
        LOG.debug("Find modules...");
        final Collection<ModuleDescription> modules = new ModuleFinder().find();
        LOG.debug("Found {} modules.", modules.size());
        return modules;
    }

    private Collection<Service> createServices(final Collection<ModuleDescription> modules) {
        final ServiceFactory factory = new ServiceFactory();
        return modules.stream()
            .map(factory::create)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
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

}

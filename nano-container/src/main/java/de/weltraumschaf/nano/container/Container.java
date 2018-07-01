package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This is the main component which manages the modules and its services.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Container {
    private static Logger LOG = LoggerFactory.getLogger(Container.class);

    private final DefaultMessageBus messages = new DefaultMessageBus();
    private volatile boolean running;
    private volatile boolean stopped;
    private Services services;

    /**
     * Start the container.
     * <p>
     * This method blocks and does not return, until {@link #stop() contianer is stopped} by an other thread.
     * </p>
     */
    public void start() {
        LOG.info("Container starts...");
        running = true;
        final Collection<ModuleDescription> modules = findModules();
        final Collection<Service> services = createServices(modules);
        this.services = new Services(services);
        this.services.start(messages);
        LOG.info("Container started.");

        loop();

        stopped = true;
    }

    /**
     * Stops the services.
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("Container never started!");
        }

        LOG.info("Container is stopping...");
        running = false;
        services.stop();
        waitUntilStopped();
        services.deactivate();
        LOG.info("Container stopped.");
    }

    private void waitUntilStopped() {
        while (!stopped) {
            try {
                LOG.debug("Wait for stopping ...");
                Thread.sleep(1_000);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }


    private Collection<Service> createServices(final Collection<ModuleDescription> modules) {
        final ServiceFactory factory = new ServiceFactory();
        return modules.stream()
            .map(factory::create)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private Collection<ModuleDescription> findModules() {
        LOG.debug("Find modules...");
        final Collection<ModuleDescription> modules = new ModuleFinder().find();
        LOG.debug("Found {} modules.", modules.size());
        return modules;
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

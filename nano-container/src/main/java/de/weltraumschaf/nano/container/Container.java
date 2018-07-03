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
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class Container {
    private static final Logger LOG = LoggerFactory.getLogger(Container.class);

    private final DefaultMessageBus messages = new DefaultMessageBus();
    private final ServiceRegistry registry = new ServiceRegistry();
    private ServiceLifecycleManager services = new ServiceLifecycleManager(registry);
    private volatile boolean running;
    private volatile boolean stopped;

    /**
     * Start the container.
     * <p>
     * This method blocks and does not return, until {@link #stop() contianer is stopped} by an other thread.
     * </p>
     */
    public void start() {
        LOG.info("Container starts...");
        running = true;
        registerServices();
        injectRequiredServices();

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
        services.stop();
        running = false;
        waitUntilStopped();
        LOG.info("Container stopped.");
    }

    private void registerServices() {
        final Collection<ModuleDescription> modules = findModules();
        final Collection<Service> created = createServices(modules);
        created.forEach(registry::register);
    }

    private void injectRequiredServices() {
        final Injector injector = new Injector(registry);
        registry.findAll().forEach(injector::injectRequired);
    }

    private void loop() {
        while (running) {
            try {
                LOG.info("Container is running.");
                Thread.sleep(5_000);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
                return;
            }
        }
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
        LOG.debug("Found {} modules: {}.",
            modules.size(), modules.stream().map(ModuleDescription::format).collect(Collectors.joining(", ")));
        return modules;
    }

}

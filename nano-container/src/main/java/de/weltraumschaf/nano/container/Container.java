package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
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

    private final ContainerContext context;
    /**
     * Volatile because must be recognized over multiple threads.
     */
    private volatile boolean running;
    /**
     * Volatile because must be recognized over multiple threads.
     */
    private volatile boolean stopped;

    /**
     * API constructor for client code to create a container.
     */
    public Container() {
        this(ContainerContext.create());
    }

    /**
     * Dedicated constructor.
     *
     * @param context not {@code null}
     */
    Container(final ContainerContext context) {
        super();
        this.context = Validate.notNull(context, "context");
    }

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

        context.getServices().start(context.getMessages());
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
        context.getServices().stop();
        running = false;
        waitUntilStopped();
        LOG.info("Container stopped.");
    }

    private void registerServices() {
        final Collection<ModuleDescription> modules = findModules();
        final Collection<Service> created = createServices(modules);
        created.forEach(service -> context.getRegistry().register(service));
    }

    private void injectRequiredServices() {
        final Injector injector = new Injector(context.getRegistry());
        context.getRegistry().findAll().forEach(injector::injectRequired);
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

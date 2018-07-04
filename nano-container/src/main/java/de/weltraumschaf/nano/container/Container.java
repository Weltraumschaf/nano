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
    private static final int SLEEP_MILLIS = 1_000;

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
            LOG.warn("Called stp, but container never started!");
            return;
        }

        LOG.info("Container is stopping...");
        context.getServices().stop();
        running = false;
        waitUntilStopped();
        LOG.info("Container stopped.");
    }

    /**
     * Whether the container is running or not.
     * <p>
     * If this method returns {@code false} does not mean that {@link #isStopped()} returns {@code true} immediately.
     * It may take some time until the container has reached ful stop.
     * </p>
     *
     * @return {@code true} is running, else {@code false}
     */
    boolean isRunning() {
        return running;
    }

    /**
     * Whether the container has reached completely stop.
     *
     * @return {@code true} if the container has shut down everything including itself, else {@code false}
     */
    boolean isStopped() {
        return stopped;
    }

    private void registerServices() {
        final Collection<ModuleDescription> modules = findModules();
        final Collection<Service> created = createServices(modules);
        created.forEach(service -> context.getRegistry().register(service));
    }

    private Collection<ModuleDescription> findModules() {
        LOG.debug("Find modules...");
        final Collection<ModuleDescription> modules = context.getFinder().find();
        final String modulesAsString = modules.stream()
            .map(ModuleDescription::format)
            .collect(Collectors.joining(", "));
        LOG.debug("Found {} modules: {}.",
            modules.size(), modulesAsString);
        return modules;
    }

    private Collection<Service> createServices(final Collection<ModuleDescription> modules) {
        return modules.stream()
            .map(module -> context.getFactory().create(module))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private void injectRequiredServices() {
        final Injector injector = new Injector(context.getRegistry());
        context.getRegistry().findAll().forEach(injector::injectRequired);
    }

    private void loop() {
        while (running) {
            LOG.info("Container is running.");
            sleep();
        }
    }

    private void waitUntilStopped() {
        while (!stopped) {
            LOG.debug("Wait for stopping ...");
            sleep();
        }
    }


    private void sleep() {
        try {
            Thread.sleep(SLEEP_MILLIS);
        } catch (final InterruptedException e) {
            LOG.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}

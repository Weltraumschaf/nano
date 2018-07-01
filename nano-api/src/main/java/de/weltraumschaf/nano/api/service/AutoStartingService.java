package de.weltraumschaf.nano.api.service;

/**
 * Auto starting services are started/stopped automatically by the container.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public interface AutoStartingService extends Service {
    /**
     * Is called async by the container right after {@link #activate(ServiceContext)}  activation}.
     * <p>
     * This method is intended to loop infinite, but it need not necessarily.
     * </p>
     */
    void start();

    /**
     * Called by the container on shutdown.
     * <p>
     * This method is called before {@link #deactivate() deactivation}.
     * </p>
     * <p>
     * If your {@link #start()} method loops infinitely you should break that loop with this method to shutdown your service.
     * </p>
     * <p>
     * This method is only called from the container, if {@link #isRunning()} returns {@code true}.
     * </p>
     */
    default void stop() {
        // Empty stub implementation.
    }

    /**
     * Whether the service is running  or not.
     * <p>
     * This method should return {@code true}, if the {@link #start()} method is intended to run indefinitely. If this
     * method returns {@code false} the container will throw away this service immediately on shutdown and will not wait
     * for the service to stop.
     * </p>
     *
     * @return {@code true} if running, else {@code false}
     */
    default boolean isRunning() {
        return false;
    }
}

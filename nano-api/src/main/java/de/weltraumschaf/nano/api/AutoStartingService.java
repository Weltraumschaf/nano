package de.weltraumschaf.nano.api;

/**
 * Auto starting services are started/stopped automatically by the container.
 *
 * @since 1.0.0
 */
public interface AutoStartingService extends Service {
    /**
     * Is called async by the container right after {@link #activate() activation}.
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
     */
    default void stop() {
        // Empty stub implementation.
    }
}

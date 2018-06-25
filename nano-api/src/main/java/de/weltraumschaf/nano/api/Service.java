package de.weltraumschaf.nano.api;

/**
 * A service is the smallest managed component known to the container.
 *
 * @since 1.0.0
 */
public interface Service {
    /**
     * Called by the container to activate the service.
     * <p>
     * This happens before the service is available to any client for usage.
     * </p>
     * <p>
     * You need not to implement this method, if your service does not need some activation stuff to do before usage.
     * </p>
     *
     * @param ctx not {@code null}
     */
    default void activate(ServiceContext ctx) {
        // Empty stub implementation.
    }

    /**
     * Called by the container to deactivate the service.
     * <p>
     * You need not to implement this method, if your service does not need some deactivation stuff to do.
     * </p>
     */
    default void deactivate() {
        // Empty stub implementation.
    }
}

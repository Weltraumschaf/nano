package de.weltraumschaf.nano.api;

/**
 * @since 1.0.0
 */
public interface AutoStartingService extends Service {
    void start();

    default void stop() {
        // Empty stub implementation.
    }
}

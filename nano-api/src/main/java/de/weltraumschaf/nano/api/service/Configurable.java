package de.weltraumschaf.nano.api.service;

/**
 * Services implementing this interface are configurable.
 *
 * @param <T> type of configuration object
 * @since 1.0.0
 */
public interface Configurable<T> {
    /**
     * Object to configure the service.
     *
     * @param callee        not {@code null}
     * @param configuration not {@code null}
     */
    void configure(Service callee, T configuration);
}

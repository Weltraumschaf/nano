package de.weltraumschaf.nano.api.service;

/**
 * Services implementing this interface are configurable.
 *
 * @since 1.0.0
 * @param <T> type of configuration object
 * @author Sven Strittmatter
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

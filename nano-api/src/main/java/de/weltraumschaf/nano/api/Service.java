package de.weltraumschaf.nano.api;

/**
 * @since 1.0.0
 */
public interface Service {
    void activate() throws Exception;
    void deactivate() throws Exception;
}

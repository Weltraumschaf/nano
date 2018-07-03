package de.weltraumschaf.nano.example.module1.api.tcp;

import de.weltraumschaf.nano.api.service.Configurable;
import de.weltraumschaf.nano.api.service.Service;

/**
 * API of a generic TCP server which can listen on  configurable TCP ports for various other services.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public interface TcpService extends Service, Configurable<TcpServiceConfiguration> {
    /**
     * Registers a handler callback for a callee server.
     *
     * @param callee  not {@code null}
     * @param handler not {@code null
     */
    void register(Service callee, TcpServiceHandler handler);

    /**
     * Start listening on the {@link #configure(Service, Object) configured port} for this callee.
     *
     * @param callee not {@code null
     */
    void start(Service callee);

    /**
     * Stop listening on the {@link #configure(Service, Object) configured port} for this callee.
     *
     * @param callee not {@code null
     */
    void stop(Service callee);

    boolean isRunning(final Service callee);

    boolean hasStopped(final Service callee);
}

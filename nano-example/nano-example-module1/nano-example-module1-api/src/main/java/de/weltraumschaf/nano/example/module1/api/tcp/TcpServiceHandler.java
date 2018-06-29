package de.weltraumschaf.nano.example.module1.api.tcp;

/**
 * Handles TCP requests.
 *
 * @since 1.0.0
 */
public interface TcpServiceHandler {
    /**
     * Called with the request data.
     *
     * @param request not {@code null}
     * @return not {@code null}, the response data
     */
    byte[] handle(byte[] request);
}

package de.weltraumschaf.nano.services.api.tcp;

/**
 * Handles TCP requests.
 *
 * @author Sven Strittmatter
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

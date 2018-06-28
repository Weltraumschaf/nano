package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.commons.validate.Validate;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @since 1.0.0
 */
@ToString
public final class TcpServiceConfig {
    @Getter
    private int port;
    @Getter
    private String hostname;

    /**
     * Convenience constructor for localhost.
     *
     * @param port must be greater than 0
     */
    public TcpServiceConfig(final int port) {
        this(port, "localhost");
    }

    /**
     * Dedicated constructor.
     *
     * @param port     must be greater than 0
     * @param hostname not {@code null} nor empty
     */
    public TcpServiceConfig(final int port, final String hostname) {
        super();
        this.port = Validate.greaterThan(port, 0, "port");
        this.hostname = Validate.notEmpty(hostname, "hostname");
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof TcpServiceConfig)) {
            return false;
        }

        final TcpServiceConfig that = (TcpServiceConfig) o;
        return port == that.port &&
            Objects.equals(hostname, that.hostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, hostname);
    }
}

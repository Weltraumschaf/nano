package de.weltraumschaf.nano.example.module1.api.tcp;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.Service;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * This object {@link TcpService#configure(Service, Object)}  configures the} from other services which use them.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
@ToString
public final class TcpServiceConfiguration {
    @Getter
    private int port;
    @Getter
    private String hostname;

    /**
     * Convenience constructor for localhost.
     *
     * @param port must be greater than 0
     */
    public TcpServiceConfiguration(final int port) {
        this(port, "localhost");
    }

    /**
     * Dedicated constructor.
     *
     * @param port     must be greater than 0
     * @param hostname not {@code null} nor empty
     */
    public TcpServiceConfiguration(final int port, final String hostname) {
        super();
        this.port = Validate.greaterThan(port, 0, "port");
        this.hostname = Validate.notEmpty(hostname, "hostname");
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof TcpServiceConfiguration)) {
            return false;
        }

        final TcpServiceConfiguration that = (TcpServiceConfiguration) o;
        return port == that.port &&
            Objects.equals(hostname, that.hostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, hostname);
    }
}

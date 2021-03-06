package de.weltraumschaf.nano.services.impl.tcp;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.Service;
import de.weltraumschaf.nano.services.api.tcp.TcpService;
import de.weltraumschaf.nano.services.api.tcp.TcpServiceConfiguration;
import de.weltraumschaf.nano.services.api.tcp.TcpServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation for TCP services.
 * <p>
 * This implementation holds internally a {@link TcpServer} for each registered callee.
 * </p>
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class DefaultTcpService implements TcpService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTcpService.class);
    private final Map<Service, TcpServiceHandler> handlers = new ConcurrentHashMap<>();
    private Map<Service, TcpServiceConfiguration> configurations = new ConcurrentHashMap<>();
    private Map<Service, TcpServer> servers = new ConcurrentHashMap<>();

    @Override
    public void configure(final Service callee, final TcpServiceConfiguration configuration) {
        this.configurations.put(
            Validate.notNull(callee, "callee"),
            Validate.notNull(configuration, "configuration")
        );
    }

    @Override
    public void register(final Service callee, final TcpServiceHandler handler) {
        handlers.put(
            Validate.notNull(callee, "callee"),
            Validate.notNull(handler, "handler")
        );
    }

    @Override
    public void start(final Service callee) {
        assertPreconditions(callee);

        final TcpServiceConfiguration configuration = configurations.get(callee);
        final int port = configuration.getPort();

        LOG.debug("Starting echo server at port {} ...", port);
        final TcpServer server = new TcpServer(configuration, handlers.get(callee));
        server.start();
        servers.put(callee, server);
        LOG.debug("Echo server started at port {} ...", port);
    }


    @Override
    public void stop(final Service callee) {
        LOG.debug("Stopping echo server ...");

        if (servers.containsKey(callee)) {
            servers.get(callee).stop();
            LOG.debug("Echo server stopped.");
        } else {
            LOG.warn("No such server started for {} to stop!", callee);
        }
    }

    @Override
    public boolean isRunning(final Service callee) {
        if (servers.containsKey(callee)) {
            return servers.get(callee).isListening();
        } else {
            return false;
        }
    }

    @Override
    public boolean hasStopped(final Service callee) {
        return !isRunning(callee);
    }

    @Override
    public void deactivate() {
        LOG.debug("Deactivating TCP service...");
        servers.values().stream().filter(TcpServer::isListening).forEach(TcpServer::stop);
        handlers.clear();
        configurations.clear();
        servers.clear();
        LOG.debug("TCP service deactivated.");
    }

    private void assertPreconditions(final Service callee) {
        if (!handlers.containsKey(callee)) {
            throw new IllegalStateException(String.format("No handler registered for %s!", callee));
        }

        if (!configurations.containsKey(callee)) {
            throw new IllegalStateException(String.format("No configuration available for %s!", callee));
        }

        if (servers.containsKey(callee)) {
            throw new IllegalStateException(String.format("Already started server for %s!", callee));
        }
    }
}

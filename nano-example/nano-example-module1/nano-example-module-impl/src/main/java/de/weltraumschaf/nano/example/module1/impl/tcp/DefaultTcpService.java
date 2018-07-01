package de.weltraumschaf.nano.example.module1.impl.tcp;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.Service;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpService;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpServiceConfiguration;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpServiceHandler;
import de.weltraumschaf.nano.example.module1.impl.echo.DefaultEchoService;
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
 * @since 1.0.0
 */
public final class DefaultTcpService implements TcpService {

    private static Logger LOG = LoggerFactory.getLogger(DefaultEchoService.class);
    private static final int BUFFER_SIZE = 1_024;
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

        LOG.debug("Starting echo server at port {0} ...", port);
        final TcpServer server = new TcpServer(configuration, handlers.get(callee));
        server.start();
        servers.put(callee, server);
        LOG.debug("Echo server started at port {0} ...", port);
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
    public void deactivate() {
        LOG.debug("Deactivating echo server...");
        servers.values().forEach(TcpServer::stop);
        handlers.clear();
        configurations.clear();
        servers.clear();
        LOG.debug("Echo server deactivated.");
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
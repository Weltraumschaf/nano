package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.api.service.Require;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.example.module1.api.EchoService;
import de.weltraumschaf.nano.services.api.tcp.TcpService;
import de.weltraumschaf.nano.services.api.tcp.TcpServiceConfiguration;

/**
 * Default implementation of an echoe server.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class DefaultEchoService implements EchoService {

    @Require
    private TcpService server;

    @Override
    public void activate(final ServiceContext ctx) {
        server.configure(this, new TcpServiceConfiguration(4242));
        server.register(this, request -> request);
    }

    @Override
    public void start() {
        server.start(this);
    }

    @Override
    public void stop() {
        server.stop(this);
    }

    @Override
    public boolean isRunning() {
        return server.isRunning(this);
    }

    @Override
    public boolean hasStopped() {
        return server.hasStopped(this);
    }
}

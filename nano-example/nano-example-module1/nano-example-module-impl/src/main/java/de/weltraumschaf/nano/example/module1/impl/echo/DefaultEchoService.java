package de.weltraumschaf.nano.example.module1.impl.echo;

import de.weltraumschaf.nano.api.Require;
import de.weltraumschaf.nano.api.ServiceContext;
import de.weltraumschaf.nano.example.module1.api.EchoService;
import de.weltraumschaf.nano.example.module1.api.TcpService;
import de.weltraumschaf.nano.example.module1.api.TcpServiceConfig;

/**
 *
 */
public final class DefaultEchoService implements EchoService {

    @Require
    private TcpService server;

    @Override
    public void activate(final ServiceContext ctx) {
        server.configure(this, new TcpServiceConfig(4242));
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
}

package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.api.service.Require;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.example.module1.api.ReverseEcho;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpService;
import de.weltraumschaf.nano.example.module1.api.tcp.TcpServiceConfiguration;

import java.nio.charset.Charset;

/**
 * Default implementation of an reverse echoe server.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class DefaultReverseEcho implements ReverseEcho {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    @Require
    private TcpService server;

    @Override
    public void activate(final ServiceContext ctx) {
        server.configure(this, new TcpServiceConfiguration(2323));
        server.register(this, this::reverse);
    }


    @Override
    public void start() {
        server.start(this);
    }

    @Override
    public void stop() {
        server.stop(this);
    }

    private byte[] reverse(final byte[] request) {
        return new StringBuilder()
            .append(new String(request, CHARSET))
            .reverse()
            .toString()
            .getBytes(CHARSET);
    }
}

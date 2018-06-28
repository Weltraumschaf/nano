package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.nano.api.Configurable;
import de.weltraumschaf.nano.api.Service;

/**
 *
 */
public interface TcpService extends Service, Configurable<TcpServiceConfiguration> {
    void register(Service callee, TcpServiceHandler handler);
    void start(Service callee);
    void stop(Service callee);
}

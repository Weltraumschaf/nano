package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.example.module1.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class DefaultHelloService implements HelloService {

    private static Logger LOG = LoggerFactory.getLogger(DefaultHelloService.class);

    @Override
    public void activate() {
        LOG.debug("Service activation.");
    }

    @Override
    public void deactivate() {
        LOG.debug("Service deactivation.");
    }

    @Override
    public void hello() {
        LOG.info("Hello, World!");
    }
}
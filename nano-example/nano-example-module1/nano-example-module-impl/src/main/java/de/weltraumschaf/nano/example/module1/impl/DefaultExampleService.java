package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.api.ServiceAdapter;
import de.weltraumschaf.nano.example.module1.api.ExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class DefaultExampleService extends ServiceAdapter implements ExampleService {

    private static Logger LOG = LoggerFactory.getLogger(DefaultExampleService.class);

    @Override
    public void activate() {
        LOG.info("Service activated.");
    }

    @Override
    public void deactivate() {
        LOG.info("Service deactivated.");
    }

    @Override
    public void hello() {
        LOG.info("Hello, World!");
    }
}

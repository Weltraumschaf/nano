package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.example.module1.api.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 *
 */
public final class DefaultAutoService implements AutoService {
    private static Logger LOG = LoggerFactory.getLogger(DefaultAutoService.class);
    private volatile boolean running;

    @Override
    public void activate() {
        LOG.debug("Service activation.");
        running = true;
    }

    @Override
    public void start() {
        LOG.debug("Starting service ...");
        final Random random = new Random();

        while (running) {
            try {
                int wait = random.nextInt(5_000);
                LOG.info("Auto (wait {}) ...", wait);
                Thread.sleep(wait);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        LOG.debug("Service stopped.");
    }

    @Override
    public void stop() {
        LOG.debug("Stopping service ...");
        running = false;
    }

    @Override
    public void deactivate() {
        LOG.debug("Service deactivation.");
    }
}

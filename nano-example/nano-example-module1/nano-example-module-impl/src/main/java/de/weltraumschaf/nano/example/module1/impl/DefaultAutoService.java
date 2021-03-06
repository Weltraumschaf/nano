package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.api.service.Require;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.example.module1.api.AutoService;
import de.weltraumschaf.nano.example.module1.api.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class DefaultAutoService implements AutoService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAutoService.class);
    private volatile boolean running;
    private volatile boolean stopped;
    @Require
    private HelperService helper;

    @Override
    public void activate(final ServiceContext ctx) {
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
                final String help = helper.help();
                LOG.info("Auto (wait {} ms): {} ...", wait, help);
                Thread.sleep(wait);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }

        stopped = true;
        LOG.debug("Service stopped.");
    }

    @Override
    public void stop() {
        LOG.debug("Stopping service ...");
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean hasStopped() {
        return stopped;
    }

    @Override
    public void deactivate() {
        LOG.debug("Service deactivation.");
    }
}

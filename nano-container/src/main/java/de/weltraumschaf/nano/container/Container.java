package de.weltraumschaf.nano.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 1.0.0
 */
public final class Container {
    private static Logger LOG = LoggerFactory.getLogger( Container.class );

    private volatile boolean running = true;

    public void start() {
        LOG.info("Container starts...");

        while (running) {
            try {
                LOG.info("Container is running.");
                Thread.sleep(1_000);
            } catch (final InterruptedException e) {
                LOG.error(e.getMessage(), e);
                return;
            }
        }
    }

    public void stop() {
        LOG.info("Container is stopping...");
        running = false;
        LOG.info("Container stopped.");
    }
}

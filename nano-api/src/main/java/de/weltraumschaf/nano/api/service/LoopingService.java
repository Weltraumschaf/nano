package de.weltraumschaf.nano.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a basic implementation for services which want to loop until {@link #stop()}.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public abstract class LoopingService implements AutoStartingService {
    private static final Logger LOG = LoggerFactory.getLogger(LoopingService.class);
    /**
     * Volatile because must be recognized over multiple threads.
     */
    private volatile boolean running;
    /**
     * Volatile because must be recognized over multiple threads.
     */
    private volatile boolean stopped;

    @Override
    public final void start() {
        LOG.debug("Start service ...");
        running = true;

        while (running) {
            doUnitOfWork();
        }

        stopped = true;
        LOG.debug("Service stopped.");
    }

    @Override
    public final void stop() {
        LOG.debug("Stop service ...");
        running = false;
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    @Override
    public final boolean hasStopped() {
        return stopped;
    }

    /**
     * This method is repeatedly called until the service is {@link #stop() stopped}.
     * <p>
     * If there is nothing to do this method should do a small busy wait.
     * </p>
     */
    protected abstract void doUnitOfWork();

}

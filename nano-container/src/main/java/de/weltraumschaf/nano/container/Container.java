package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @since 1.0.0
 */
public final class Container {
    private static Logger LOG = LoggerFactory.getLogger(Container.class);

    private volatile boolean running = true;

    public void start() {
        LOG.info("Container starts...");
        final Collection<ModuleDescription> modules = findModules();

        loop();
    }


    public void stop() {
        LOG.info("Container is stopping...");
        running = false;
        LOG.info("Container stopped.");
    }

    private Collection<ModuleDescription> findModules() {
        LOG.debug("Find modules...");
        final Collection<ModuleDescription> modules = new ModuleFinder().find();
        LOG.debug("Found {} modules.", modules.size());
        return modules;
    }

    private void loop() {
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
}

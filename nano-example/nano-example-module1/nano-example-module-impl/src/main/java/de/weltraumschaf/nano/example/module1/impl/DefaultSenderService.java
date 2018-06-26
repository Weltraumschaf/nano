package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.ServiceContext;
import de.weltraumschaf.nano.api.messaging.Message;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.example.module1.api.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class DefaultSenderService implements SenderService {
    private static Logger LOG = LoggerFactory.getLogger(DefaultSenderService.class);
    private MessageBus messages;
    private volatile boolean running;

    @Override
    public void activate(final ServiceContext ctx) {
        LOG.debug("Service activation.");
        messages = Validate.notNull(ctx, "ctx").getMessages();
        running = true;
    }

    @Override
    public void start() {
        LOG.debug("Starting service ...");

        while (running) {
            try {
                messages.publish(Topics.MY_TOPIC, new Message());
                Thread.sleep(500);
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

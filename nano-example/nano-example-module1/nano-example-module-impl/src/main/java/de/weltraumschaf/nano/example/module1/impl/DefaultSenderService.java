package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.LoopingService;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.api.messaging.Message;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.example.module1.api.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class DefaultSenderService extends LoopingService implements SenderService {
    private static Logger LOG = LoggerFactory.getLogger(DefaultSenderService.class);
    private MessageBus messages;

    @Override
    public void activate(final ServiceContext ctx) {
        LOG.debug("Service activation.");
        messages = Validate.notNull(ctx, "ctx").getMessages();
    }

    @Override
    protected void doUnitOfWork() {
        try {
            messages.publish(new Message(Topics.MY_TOPIC, "Message in a bottle..."));
            Thread.sleep(5_000);
        } catch (final InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }


    @Override
    public void deactivate() {
        LOG.debug("Service deactivation.");
    }
}

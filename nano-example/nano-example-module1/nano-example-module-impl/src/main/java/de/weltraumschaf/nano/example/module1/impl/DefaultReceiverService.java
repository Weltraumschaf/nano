package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.Message;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.messaging.MessageSubscriber;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.example.module1.api.ReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class DefaultReceiverService implements ReceiverService, MessageSubscriber {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReceiverService.class);
    private MessageBus messages;

    @Override
    public void activate(final ServiceContext ctx) {
        LOG.debug("Service activation.");
        messages = Validate.notNull(ctx, "ctx").getMessages();
        messages.subscribe(Topics.MY_TOPIC, this);
    }

    @Override
    public void deactivate() {
        LOG.debug("Service deactivation.");
        messages.unsubscribe(this);
    }

    @Override
    public void receive(final Message message) {
        LOG.debug("Received message: {}", message);
    }
}

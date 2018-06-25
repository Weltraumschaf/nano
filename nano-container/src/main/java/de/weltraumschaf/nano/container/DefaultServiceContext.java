package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.ServiceContext;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import lombok.ToString;

/**
 * Default implementation.
 *
 * @since 1.0.0
 */
@ToString
final class DefaultServiceContext implements ServiceContext {
    private final MessageBus messages;

    DefaultServiceContext(final MessageBus messages) {
        super();
        this.messages = Validate.notNull(messages, "messages");
    }

    @Override
    public MessageBus messages() {
        return messages;
    }
}

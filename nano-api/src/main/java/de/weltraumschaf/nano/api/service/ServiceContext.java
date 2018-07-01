package de.weltraumschaf.nano.api.service;

import de.weltraumschaf.nano.api.messaging.MessageBus;

/**
 * Implementations provide things a service may need to do its work.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface ServiceContext {
    /**
     * Returns the containers message bus.
     *
     * @return never {@code null}
     */
    MessageBus getMessages();
}

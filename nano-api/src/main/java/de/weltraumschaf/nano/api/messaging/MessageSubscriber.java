package de.weltraumschaf.nano.api.messaging;

/**
 * A subscriber can tell interest of getMessages.
 *
 * @since 1.0.0
 */
public interface MessageSubscriber {
    void receive(Message message);
}

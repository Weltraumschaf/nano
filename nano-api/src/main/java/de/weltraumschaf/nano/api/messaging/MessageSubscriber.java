package de.weltraumschaf.nano.api.messaging;

/**
 * A subscriber can tell interest of {@link Message messages}.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface MessageSubscriber {
    /**
     * THis message will be called if the implementor is subscribed for messages.
     *
     * @param message not {@code null}
     */
    void receive(Message message);
}

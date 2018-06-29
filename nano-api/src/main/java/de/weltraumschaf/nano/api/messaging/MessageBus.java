package de.weltraumschaf.nano.api.messaging;

/**
 * Defines the interface of an async message bus.
 * <p>
 * A publisher can send getMessages to a topic and subscriber will receive this message.
 * </p>
 *
 * @since 1.0.0
 */
public interface MessageBus {
    /**
     * Subscribes the subscriber to a particular topic.
     *
     * @param topic not {@code null}
     * @param subscriber not {@code null}
     */
    void subscribe(MessageTopic topic, MessageSubscriber subscriber);

    /**
     * Unsubscribe a subscriber from a particular topic.
     *
     * @param subscriber not {@code null}
     */
    void unsubscribe(MessageSubscriber subscriber);

    /**
     * Publishes a massage to a particular topic.
     *
     * @param topic not {@code null}
     * @param message not {@code null}
     */
    void publish(MessageTopic topic, Message message);
}

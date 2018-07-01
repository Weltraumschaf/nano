package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.Message;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.messaging.MessageSubscriber;
import de.weltraumschaf.nano.api.messaging.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default implementation.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
final class DefaultMessageBus implements MessageBus {
    private static final int THREADS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMessageBus.class);
    private final Map<MessageTopic, Collection<MessageSubscriber>> subscribers = new ConcurrentHashMap<>();
    private final ExecutorService pool = Executors.newFixedThreadPool(THREADS);

    @Override
    public void subscribe(final MessageTopic topic, final MessageSubscriber subscriber) {
        Validate.notNull(topic, "topic");
        Validate.notNull(subscriber, "subscriber");
        LOG.debug("Subscribe '{}' to topic '{}'.", subscriber.getClass().getCanonicalName(), topic);
        subscribers.computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>()).add(subscriber);
    }

    @Override
    public void unsubscribe(final MessageSubscriber subscriber) {
        Validate.notNull(subscriber, "subscriber");
        LOG.debug("Unsubscribe '{}'.", subscriber.getClass().getCanonicalName());
        subscribers.forEach((k, v) -> v.remove(subscriber));
    }

    @Override
    public void publish(final Message message) {
        Validate.notNull(message, "message");
        final MessageTopic topic = message.getTopic();
        LOG.debug("Publish message {} to topic {}.", message, topic);

        final Collection<MessageSubscriber> determined = determineSubscribers(topic);
        notifySubscribers(message, determined);
    }

    private Collection<MessageSubscriber> determineSubscribers(final MessageTopic topic) {
        return this.subscribers.computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>());
    }

    private void notifySubscribers(final Message message, final Collection<MessageSubscriber> subscribersOfTopic) {
        pool.execute(() -> subscribersOfTopic.forEach(subscriber -> subscriber.receive(message)));
    }
}

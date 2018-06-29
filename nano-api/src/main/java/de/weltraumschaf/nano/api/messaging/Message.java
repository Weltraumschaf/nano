package de.weltraumschaf.nano.api.messaging;

import de.weltraumschaf.commons.validate.Validate;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * Immutable class to encapsulate a sent around message.
 *
 * @since 1.0.0
 */
@ToString
public final class Message {
    @Getter
    private MessageTopic topic;

    /**
     * Dedicated constructor.
     *
     * @param topic not {@code null}
     */
    public Message(final MessageTopic topic) {
        super();
        this.topic = Validate.notNull(topic, "topic");
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Message)) {
            return false;
        }

        final Message message = (Message) o;
        return Objects.equals(topic, message.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic);
    }
}

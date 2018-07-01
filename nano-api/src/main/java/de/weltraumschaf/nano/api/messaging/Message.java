package de.weltraumschaf.nano.api.messaging;

import de.weltraumschaf.commons.validate.Validate;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * Immutable class to encapsulate a sent around message.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@ToString
public final class Message {
    /**
     * Topic of the message (never {@code null}.
     */
    @Getter
    private MessageTopic topic;
    /**
     * Content of the message (never {@code null}.
     */
    @Getter
    private String content;

    /**
     * Dedicated constructor.
     *
     * @param topic not {@code null}
     */
    public Message(final MessageTopic topic, final String content) {
        super();
        this.topic = Validate.notNull(topic, "topic");
        this.content = Validate.notNull(content, "content");
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Message)) {
            return false;
        }

        final Message message = (Message) o;
        return Objects.equals(topic, message.topic) &&
            Objects.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, content);
    }
}

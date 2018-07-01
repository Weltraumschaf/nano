package de.weltraumschaf.nano.api.messaging;

/**
 * Marker interface for {@link Message message} topics,
 * <p>
 * Implement this interface with an enum to define your topics:
 * </p>
 * <pre>{@code
 * enum Topics implements MessageTopic {
 *     MY_TOPIC,
 *     MY_OTHER_TOPIC;
 * }
 * }</pre>
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface MessageTopic {
}

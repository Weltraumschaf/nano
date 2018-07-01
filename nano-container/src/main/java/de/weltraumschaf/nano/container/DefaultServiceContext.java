package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * Default implementation.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@ToString
final class DefaultServiceContext implements ServiceContext {
    @Getter
    private final MessageBus messages;

    DefaultServiceContext(final MessageBus messages) {
        super();
        this.messages = Validate.notNull(messages, "getMessages");
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultServiceContext)) {
            return false;
        }

        final DefaultServiceContext that = (DefaultServiceContext) o;
        return Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages);
    }
}

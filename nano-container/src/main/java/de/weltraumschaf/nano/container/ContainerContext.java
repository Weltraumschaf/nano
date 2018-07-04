package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import lombok.Getter;
import lombok.ToString;

/**
 * Context to carry around internal container global state.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
@ToString
final class ContainerContext {
    @Getter
    private final MessageBus messages;
    @Getter
    private final ServiceRegistry registry;
    @Getter
    private final ServiceLifecycleManager services;

    /**
     * Dedicated constructor.
     *
     * @param messages not {@code null}
     * @param registry not {@code null}
     * @param services not {@code null}
     */
    ContainerContext(final MessageBus messages, final ServiceRegistry registry, final ServiceLifecycleManager services) {
        super();
        this.messages = Validate.notNull(messages, "messages");
        this.registry = Validate.notNull(registry, "registry");
        this.services = Validate.notNull(services, "services");
    }

    /**
     * Creates a new ready to use initialized context.
     *
     * @return never {@code null}
     */
    static ContainerContext create() {
        final ServiceRegistry registry = new ServiceRegistry();
        return new ContainerContext(new DefaultMessageBus(), registry, new ServiceLifecycleManager(registry));
    }

}

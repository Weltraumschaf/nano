package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import lombok.Getter;
import lombok.ToString;

/**
 * Context to carry around internal container global state.
 * <p>
 * This class is immutable.
 * </p>
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
    @Getter
    private final ModuleFinder finder;
    @Getter
    private final ServiceFactory factory;
    @Getter
    private final Injector injector;

    /**
     * Dedicated constructor.
     *
     * @param messages not {@code null}
     * @param registry not {@code null}
     * @param services not {@code null}
     * @param finder   not {@code null}
     * @param factory  not {@code null}
     * @param injector not {@code null}
     */
    ContainerContext(final MessageBus messages, final ServiceRegistry registry, final ServiceLifecycleManager services, final ModuleFinder finder, final ServiceFactory factory, final Injector injector) {
        super();
        this.messages = Validate.notNull(messages, "messages");
        this.registry = Validate.notNull(registry, "registry");
        this.services = Validate.notNull(services, "services");
        this.finder = Validate.notNull(finder, "finder");
        this.factory = Validate.notNull(factory, "factory");
        this.injector = Validate.notNull(injector, "injector");
    }

    /**
     * Creates a new ready to use initialized context.
     *
     * @return never {@code null}
     */
    static ContainerContext create() {
        final ServiceRegistry registry = new ServiceRegistry();
        return new ContainerContext(
            new DefaultMessageBus(),
            registry,
            new ServiceLifecycleManager(registry),
            new DefaultModuleFinder(),
            new DefaultServiceFactory(),
            new Injector(registry));
    }

    /**
     * Mutator method which copies the object but with new {@code messages}.
     *
     * @param messages not {@code null}
     * @return never {@code null}, new instance
     */
    ContainerContext withMessages(final MessageBus messages) {
        return new ContainerContext(
            messages,
            getRegistry(),
            getServices(),
            getFinder(),
            getFactory(),
            getInjector());
    }

    /**
     * Mutator method which copies the object but with new {@code registry}.
     *
     * @param registry not {@code null}
     * @return never {@code null}, new instance
     */
    ContainerContext withRegistry(final ServiceRegistry registry) {
        return new ContainerContext(
            getMessages(),
            registry,
            getServices(),
            getFinder(),
            getFactory(),
            getInjector());
    }

    /**
     * Mutator method which copies the object but with new {@code services}.
     *
     * @param services not {@code null}
     * @return never {@code null}, new instance
     */
    ContainerContext withLifecycleManager(final ServiceLifecycleManager services) {
        return new ContainerContext(
            getMessages(),
            getRegistry(),
            services,
            getFinder(),
            getFactory(),
            getInjector());
    }

    /**
     * Mutator method which copies the object but with new {@code finder}.
     *
     * @param finder not {@code null}
     * @return never {@code null}, new instance
     */
    ContainerContext withFinder(final ModuleFinder finder) {
        return new ContainerContext(
            getMessages(),
            getRegistry(),
            getServices(),
            finder,
            getFactory(),
            getInjector());
    }

    /**
     * Mutator method which copies the object but with new {@code factory}.
     *
     * @param factory not {@code null}
     * @return never {@code null}, new instance
     */
    ContainerContext withFactory(final ServiceFactory factory) {
        return new ContainerContext(
            getMessages(),
            getRegistry(),
            getServices(),
            getFinder(),
            factory,
            getInjector());
    }

    /**
     * Mutator method which copies the object but with new {@code injector}.
     *
     * @param injector not {@code null}
     * @return never {@code null}, new instance
     */
    ContainerContext withInjector(final Injector injector) {
        return new ContainerContext(
            getMessages(),
            getRegistry(),
            getServices(),
            getFinder(),
            getFactory(),
            injector);
    }
}

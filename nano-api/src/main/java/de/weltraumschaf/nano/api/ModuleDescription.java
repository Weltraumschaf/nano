package de.weltraumschaf.nano.api;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.service.Service;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

/**
 * Describes a module.
 * <p>
 * A module as a unique id, a name, a description and a set of service interfaces.
 * </p>
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
@ToString
public final class ModuleDescription {
    @Getter
    private final UUID id;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Set<Class<? extends Service>> services;

    /**
     * Convenience constructor which initializes the id with {@link UUID#randomUUID()}.
     *
     * @param name        not {@code null} nor empty
     * @param description not {@code null}
     * @param services    not {@code null}
     */
    public ModuleDescription(final String name, final String description, final Set<Class<? extends Service>> services) {
        this(UUID.randomUUID(), name, description, services);
    }

    /**
     * Dedicated constructor.
     *
     * @param id          not {@code null}
     * @param name        not {@code null} nor empty
     * @param description not {@code null}
     * @param services    not {@code null}
     */
    public ModuleDescription(final UUID id, final String name, final String description, final Collection<Class<? extends Service>> services) {
        super();
        this.id = Validate.notNull(id, "id");
        this.name = Validate.notEmpty(name, "name");
        this.description = Validate.notNull(description, "description");
        this.services = new HashSet<>(Validate.notNull(services, "services"));
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ModuleDescription)) {
            return false;
        }

        final ModuleDescription that = (ModuleDescription) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(services, that.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, services);
    }

    public String format() {
        return String.format("'%s' (%s)", name, id);
    }
}

package de.weltraumschaf.nano.api;

import de.weltraumschaf.commons.validate.Validate;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

/**
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
    private final Collection<Class<? extends Service>> services;

    public ModuleDescription(final String name, final String description, final Collection<Class<? extends Service>> services) {
        this(UUID.randomUUID(), name, description, services);
    }

    public ModuleDescription(final UUID id, final String name, final String description, final Collection<Class<? extends Service>> services) {
        super();
        this.id = Validate.notNull(id, "id");
        this.name = Validate.notEmpty(name, "name");
        this.description = Validate.notNull(description, "description");
        this.services = new ArrayList<>(Validate.notNull(services, "services"));
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

}

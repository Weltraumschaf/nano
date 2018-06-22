package de.weltraumschaf.nano.api;

import de.weltraumschaf.commons.validate.Validate;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;

/**
 * @since 1.0.0
 */
@ToString
public final class ServiceDescription {
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Collection<Class<? extends Service>> services;

    public ServiceDescription(final String name, final String description, final Collection<Class<? extends Service>> services) {
        super();
        this.name = Validate.notEmpty(name, "name");
        this.description = Validate.notNull(description, "description");
        this.services = Collections.unmodifiableCollection(Validate.notNull(services, "services"));
    }
}

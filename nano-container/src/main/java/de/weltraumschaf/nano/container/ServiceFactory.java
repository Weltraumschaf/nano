package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.service.Service;

import java.util.Collection;

/**
 * Creates the services.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
interface ServiceFactory {
    /**
     * Creates the services for a given module.
     *
     * @param module not {@code null}
     * @return never {2code null}
     */
    Collection<Service> create(ModuleDescription module);
}

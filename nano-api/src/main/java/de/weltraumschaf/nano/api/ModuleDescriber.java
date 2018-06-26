package de.weltraumschaf.nano.api;

import de.weltraumschaf.commons.validate.Validate;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.Set;

/**
 * Implementations describe a module.
 * <p>
 * Each Module API must contain one implementation of this interface and file {@literal META-INF/services/de.weltraumschaf.nano.api.ModuleDescriber} which contains the canonical class name of that implementation.
 * </p>
 *
 * @since 1.0.0
 */
public interface ModuleDescriber {
    /**
     * Describes the module.
     *
     * @return never {@code null}
     */
    ModuleDescription describe();

    /**
     * Helper method to find all interfaces in a given package extending {@link Service}.
     *
     * @param packageName not {@code null} nor empty
     * @return never {@code null}, unmodifiable
     */
    static Set<Class<? extends Service>> findServices(final String packageName) {
        Validate.notEmpty(packageName, "packageName");
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<? extends Service>> found = reflections.getSubTypesOf(Service.class);

        if (found.remove(AutoStartingService.class)) {
            found.addAll(reflections.getSubTypesOf(AutoStartingService.class));
        }

        return Collections.unmodifiableSet(found);
    }
}

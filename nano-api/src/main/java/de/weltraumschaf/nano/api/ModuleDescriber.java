package de.weltraumschaf.nano.api;

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
}

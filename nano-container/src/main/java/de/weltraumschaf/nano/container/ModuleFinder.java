package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescriber;
import de.weltraumschaf.nano.api.ModuleDescription;

import java.util.*;

/**
 * Finds all available modules in the class path via via <a href="https://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html">SPI</a>.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class ModuleFinder {
    /**
     * Finds all modules.
     *
     * @return never {@code null}, unmodifiable
     */
    Collection<ModuleDescription> find() {
        final ServiceLoader<ModuleDescriber> describers = ServiceLoader.load(ModuleDescriber.class);
        final Collection<ModuleDescription> descriptions = new ArrayList<>();

        for (final ModuleDescriber describer : describers) {
            descriptions.add(describer.describe());
        }

        return Collections.unmodifiableCollection(descriptions);
    }
}

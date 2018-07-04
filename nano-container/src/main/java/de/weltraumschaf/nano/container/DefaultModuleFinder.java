package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescriber;
import de.weltraumschaf.nano.api.ModuleDescription;

import java.util.*;

/**
 * Default implementation.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class DefaultModuleFinder implements ModuleFinder {
    @Override
    public Collection<ModuleDescription> find() {
        final ServiceLoader<ModuleDescriber> describers = ServiceLoader.load(ModuleDescriber.class);
        final Collection<ModuleDescription> descriptions = new ArrayList<>();

        for (final ModuleDescriber describer : describers) {
            descriptions.add(describer.describe());
        }

        return Collections.unmodifiableCollection(descriptions);
    }
}

package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.nano.api.Service;
import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.ModuleDescriber;

import java.util.Collection;
import java.util.Collections;

/**
 * Describes this module.
 */
public final class Describer implements ModuleDescriber {
    @Override
    public ModuleDescription describe() {
        final Collection<Class<? extends Service>> services = Collections.singletonList(ExampleService.class);
        return new ModuleDescription("Example Module 1", "", services);
    }
}

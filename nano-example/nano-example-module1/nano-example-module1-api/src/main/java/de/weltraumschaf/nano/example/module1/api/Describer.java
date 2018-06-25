package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.nano.api.ModuleDescriber;
import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.Service;

import java.util.Arrays;
import java.util.Collection;

/**
 * Describes this module.
 */
public final class Describer implements ModuleDescriber {
    @Override
    public ModuleDescription describe() {
        final Collection<Class<? extends Service>> services = Arrays.asList(HelloService.class, AutoService.class);
        return new ModuleDescription("Example Module 1", "", services);
    }
}

package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.nano.api.ModuleDescriber;
import de.weltraumschaf.nano.api.ModuleDescription;

/**
 * Describes this module.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class Describer implements ModuleDescriber {

    @Override
    public ModuleDescription describe() {
        return new ModuleDescription(
            "Example Module 1",
            "",
            ModuleDescriber.findServices("de.weltraumschaf.nano.example.module1.api"));
    }
}

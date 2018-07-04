package de.weltraumschaf.nano.services.api;

import de.weltraumschaf.nano.api.ModuleDescriber;
import de.weltraumschaf.nano.api.ModuleDescription;

/**
 * Describes Nano Container default services module.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class NanoServicesModule implements ModuleDescriber {

    @Override
    public ModuleDescription describe() {
        return new ModuleDescription(
            "Nano Container Services",
            "This module provides some useful reusable services.",
            ModuleDescriber.findServices("de.weltraumschaf.nano.services.api"));
    }
}

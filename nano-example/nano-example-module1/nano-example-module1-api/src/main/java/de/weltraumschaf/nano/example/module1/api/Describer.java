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
        return new ModuleDescription(
            "Example Module 1",
            "",
            ModuleDescriber.findServices("de.weltraumschaf.nano.example.module1.api"));
    }
}

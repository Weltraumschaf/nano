package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescriber;
import de.weltraumschaf.nano.api.ModuleDescription;

import java.util.Collections;
import java.util.UUID;

/**
 * This describer is to test the {@link ModuleFinder module finder}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class FixtureModuleTwo implements ModuleDescriber {
    @Override
    public ModuleDescription describe() {
        return new ModuleDescription(
            UUID.fromString("ac4b35aa-6155-4f27-aeed-7707c5a50a65"),
            "fixture-module-two",
            "This is a test module.",
            Collections.emptyList());
    }
}

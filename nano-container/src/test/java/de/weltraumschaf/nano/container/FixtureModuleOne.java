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
public final class FixtureModuleOne implements ModuleDescriber {
    @Override
    public ModuleDescription describe() {
        return new ModuleDescription(
            UUID.fromString("06260abf-03cd-463c-9169-b2094822791b"),
            "fixture-module-one",
            "This is a test module.",
            Collections.emptyList());
    }
}

package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ModuleFinder}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class ModuleFinderTest {
    private final ModuleFinder sut = new ModuleFinder();

    @Test
    public void find() {
        final Collection<ModuleDescription> modules = sut.find();

        assertThat(modules, containsInAnyOrder(
            new ModuleDescription(
                UUID.fromString("06260abf-03cd-463c-9169-b2094822791b"),
                "fixture-module-one",
                "This is a test module.",
                Collections.emptyList()),
            new ModuleDescription(
                UUID.fromString("ac4b35aa-6155-4f27-aeed-7707c5a50a65"),
                "fixture-module-two",
                "This is a test module.",
                Collections.emptyList())
        ));
    }
}

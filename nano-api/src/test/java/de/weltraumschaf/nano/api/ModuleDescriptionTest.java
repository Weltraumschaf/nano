package de.weltraumschaf.nano.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link ModuleDescription}.
 */
public class ModuleDescriptionTest {
    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(ModuleDescription.class).verify();
    }
}

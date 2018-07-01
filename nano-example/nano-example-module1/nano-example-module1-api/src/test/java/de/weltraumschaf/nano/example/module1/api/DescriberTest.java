package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.nano.api.ModuleDescription;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Describer}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class DescriberTest {

    private final Describer sut = new Describer();

    @Test
    public void describe() {
        final ModuleDescription description = sut.describe();

        assertThat(description, is(not(nullValue())));
        assertThat(description.getId(), is(not(nullValue())));
        assertThat(description.getName(), is("Example Module 1"));
        assertThat(description.getDescription(), is(""));
        assertThat(description.getServices(), hasSize(7));
    }
}
package de.weltraumschaf.nano.api;

import de.weltraumschaf.nano.api.service.Service;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ModuleDescription}.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public class ModuleDescriptionTest {

    private final UUID id = UUID.fromString("7f0fbe47-87bd-4280-b9f7-b7badaa81f43");
    private final Class<Foo> foo = Foo.class;
    private final Class<Bar> bar = Bar.class;
    private final Class<Baz> baz = Baz.class;
    private final HashSet<Class<? extends Service>> services = new HashSet<>(Arrays.asList(
        foo, bar, baz
    ));
    private final ModuleDescription sut = new ModuleDescription(
        id,
        "name",
        "desc",
        services);

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(ModuleDescription.class).verify();
    }

    @Test
    public void getId() {
        assertThat(sut.getId(), is(id));
    }

    @Test
    public void getName() {
        assertThat(sut.getName(), is("name"));
    }

    @Test
    public void getDescription() {
        assertThat(sut.getDescription(), is("desc"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getServices() {
        assertThat(sut.getServices(), containsInAnyOrder(foo, bar, baz));
    }

    @Test
    public void format() {
        assertThat(sut.format(), is("'name' (7f0fbe47-87bd-4280-b9f7-b7badaa81f43)"));
    }

    interface Foo extends Service {
    }

    interface Bar extends Service {
    }

    interface Baz extends Service {
    }
}

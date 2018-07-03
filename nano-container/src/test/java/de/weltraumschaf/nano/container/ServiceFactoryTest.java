package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import org.junit.Test;

import java.util.Collections;

/**
 * Tests for {@link ServiceFactory}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class ServiceFactoryTest {
    private final ServiceFactory sut = new ServiceFactory();

    @Test(expected = NullPointerException.class)
    public void create_notNull() {
        sut.create(null);
    }

    @Test(expected = IllegalStateException.class)
    public void create_noImplementationFound() {
        sut.create(new ModuleDescription(
            "name", "desc", Collections.singleton(HasNoImplementation.class)));
    }

    @Test(expected = IllegalStateException.class)
    public void create_moreThanOneImplementationFound() {
        sut.create(new ModuleDescription(
            "name", "desc", Collections.singleton(HasTwoImplementation.class)));
    }
}
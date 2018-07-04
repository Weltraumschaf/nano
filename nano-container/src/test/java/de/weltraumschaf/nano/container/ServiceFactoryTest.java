package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.service.Service;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DefaultServiceFactory}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class ServiceFactoryTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private final ServiceFactory sut = new DefaultServiceFactory();

    @Test(expected = NullPointerException.class)
    public void create_notNull() {
        sut.create(null);
    }

    @Test
    public void create_noImplementationFound() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("No implementation found for service API " +
            "'de.weltraumschaf.nano.container.ServiceFactoryTest.HasNoImplementation'!");

        sut.create(new ModuleDescription(
            "name", "desc", Collections.singleton(HasNoImplementation.class)));
    }

    @Test
    public void create_moreThanOneImplementationFound() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("More than one implementation found for service API " +
            "'de.weltraumschaf.nano.container.ServiceFactoryTest.HasTwoImplementation'! " +
            "Can't decide which one to use.");

        sut.create(new ModuleDescription(
            "name", "desc", Collections.singleton(HasTwoImplementation.class)));
    }

    @Test
    public void create() {
        final Collection<Service> services = sut.create(
            new ModuleDescription(
                "name",
                "desc",
                new HashSet<>(Arrays.asList(ServiceOne.class, ServiceTwo.class))));

        assertThat(services, containsInAnyOrder(
            new DefaultServiceOne(), new DefaultServiceTwo()
        ));
    }

    interface HasNoImplementation extends Service {
    }

    public interface HasTwoImplementation extends Service {
    }

    public static final class HasTwoImplementationImplOne implements HasTwoImplementation {
    }

    public static final class HasTwoImplementationImplTwo implements HasTwoImplementation {
    }

    public interface ServiceOne extends Service {
    }

    public static final class DefaultServiceOne implements ServiceOne {
        // Equals/hashCode so we can compare easy in tests.
        @Override
        public boolean equals(final Object o) {
            return o instanceof DefaultServiceOne;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    public interface ServiceTwo extends Service {
    }

    public static final class DefaultServiceTwo implements ServiceTwo {
        // Equals/hashCode so we can compare easy in tests.
        @Override
        public boolean equals(final Object o) {
            return o instanceof DefaultServiceTwo;
        }

        @Override
        public int hashCode() {
            return 2;
        }
    }

}
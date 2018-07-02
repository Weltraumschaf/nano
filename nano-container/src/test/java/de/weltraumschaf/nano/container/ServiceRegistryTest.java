package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.service.AutoStartingService;
import de.weltraumschaf.nano.api.service.Service;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ServiceRegistry}.
 */
public class ServiceRegistryTest {
    private final ServiceRegistry sut = new ServiceRegistry();

    @Test(expected = NullPointerException.class)
    public void register_notNull() {
        sut.register(null);
    }

    @Test
    public void findAll() {
        final Service one = mock(Service.class);
        sut.register(one);
        final Service two = mock(Service.class);
        sut.register(two);
        final Service three = mock(Service.class);
        sut.register(three);

        assertThat(sut.findAll(), containsInAnyOrder(one, two, three));
    }

    @Test
    public void findAutoStarting() {
        final Service one = mock(AutoStartingService.class);
        sut.register(one);
        final Service two = mock(Service.class);
        sut.register(two);
        final Service three = mock(AutoStartingService.class);
        sut.register(three);

        assertThat(sut.findAutoStarting(), containsInAnyOrder(one, three));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void findOne() {
        final ServiceOne one = mock(ServiceOne.class);
        sut.register(one);
        final ServiceTwo two = mock(ServiceTwo.class);
        sut.register(two);

        assertThat(sut.findOne(ServiceOne.class).get(), is(sameInstance(one)));
        assertThat(sut.findOne(ServiceTwo.class).get(), is(sameInstance(two)));
    }

    interface ServiceOne extends Service {
    }

    interface ServiceTwo extends AutoStartingService {
    }
}
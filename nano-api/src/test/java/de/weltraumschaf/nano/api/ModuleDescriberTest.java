package de.weltraumschaf.nano.api;

import de.weltraumschaf.nano.api.service.Service;
import de.weltraumschaf.nano.api.services2find.ServiceOne;
import de.weltraumschaf.nano.api.services2find.ServiceThree;
import de.weltraumschaf.nano.api.services2find.ServiceTwo;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ModuleDescriber}.
 */
public class ModuleDescriberTest {

    @Test
    @SuppressWarnings("unchecked")
    public void findServices() {
        final Set<Class<? extends Service>> services =
            ModuleDescriber.findServices("de.weltraumschaf.nano.api.services2find");

        assertThat(services, containsInAnyOrder(ServiceOne.class, ServiceTwo.class, ServiceThree.class));
    }
}
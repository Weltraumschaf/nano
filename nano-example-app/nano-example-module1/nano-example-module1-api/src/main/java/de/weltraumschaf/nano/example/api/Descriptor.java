package de.weltraumschaf.nano.example.api;

import de.weltraumschaf.nano.api.Service;
import de.weltraumschaf.nano.api.ServiceDescription;
import de.weltraumschaf.nano.api.ServiceDescriptor;

import java.util.Collection;
import java.util.Collections;

/**
 *
 */
public final class Descriptor implements ServiceDescriptor {
    @Override
    public ServiceDescription describe() {
        final Collection<Class<? extends Service>> services = Collections.singletonList(ExampleService.class);
        return new ServiceDescription("Example Service", "", services);
    }
}

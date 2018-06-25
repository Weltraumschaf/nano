package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Activates the services.
 *
 * @since 1.0.0
 */
final class ServiceActivator {
    private static Logger LOG = LoggerFactory.getLogger(ServiceActivator.class);

    Collection<Service> activate(final ModuleDescription module) {
        LOG.debug("Activate services for module {} ({}) ...", module.getName(), module.getId());
        final Collection<Service> services = findServices(module.getServices());
        LOG.debug("Found {} service to activate ...", services.size());
        services.forEach(Service::activate);
        LOG.debug("All services for module {} ({}) activated.", module.getName(), module.getId());
        return services;
    }

    private Collection<Service> findServices(final Collection<Class<? extends Service>> interfaces) {
        return interfaces.stream().map(api -> {
            final Iterator<? extends Service> iterator = ServiceLoader.load(api).iterator();

            if (!iterator.hasNext()) {
                throw new IllegalStateException(String.format("No implementation found for service API '%s'!", api));
            }

            final Service first = iterator.next();

            if (iterator.hasNext()) {
                throw new IllegalStateException(
                    String.format("More than one implementation found for service API '%s'! Can't decide which one to use.",
                        api));
            }

            return first;
        }).collect(Collectors.toList());
    }
}

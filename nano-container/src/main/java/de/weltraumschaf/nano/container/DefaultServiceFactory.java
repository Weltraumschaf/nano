package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.nano.api.ModuleDescription;
import de.weltraumschaf.nano.api.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Default implementation.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class DefaultServiceFactory implements ServiceFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultServiceFactory.class);

    @Override
    public Collection<Service> create(final ModuleDescription module) {
        Validate.notNull(module, "module");
        String moduleFormatted = module.format();
        LOG.debug("Create services for module {} ...", moduleFormatted);
        final Collection<Service> services = create(module.getServices());
        final String servicesAsString = services.stream()
            .map(Service::toString)
            .collect(Collectors.joining(", "));
        LOG.debug("Created {} service: {}.", services.size(), servicesAsString);
        return services;
    }

    private Collection<Service> create(final Collection<Class<? extends Service>> interfaces) {
        return interfaces.stream().map(api -> {
            final Iterator<? extends Service> iterator = ServiceLoader.load(api).iterator();

            if (!iterator.hasNext()) {
                throw new IllegalStateException(
                    String.format("No implementation found for service API '%s'!", api.getCanonicalName()));
            }

            final Service first = iterator.next();

            if (iterator.hasNext()) {
                throw new IllegalStateException(
                    String.format(
                        "More than one implementation found for service API '%s'! " +
                            "Can't decide which one to use.",
                        api.getCanonicalName()));
            }

            return first;
        }).collect(Collectors.toList());
    }
}

package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.AutoStartingService;
import de.weltraumschaf.nano.api.Require;
import de.weltraumschaf.nano.api.Service;
import de.weltraumschaf.nano.api.ServiceContext;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link Services}.
 */
public class ServicesTest {
    private final ServiceOne serviceOne = mock(ServiceOne.class);
    private final ServiceTwo serviceTwo = mock(ServiceTwo.class);
    private final DefaultServiceThree serviceThree = spy(new DefaultServiceThree());
    private final Services sut = new Services(Arrays.asList(
        serviceOne, serviceTwo, serviceThree));

    @Test(expected = NullPointerException.class)
    public void start_messagesNotNull() {
        sut.start(null);
    }

    @Test
    public void start_injectRequiredServices() {
        sut.start(mock(MessageBus.class));

        assertThat(serviceThree.one, is(serviceOne));
        assertThat(serviceThree.two, is(serviceTwo));
    }

    @Test
    public void start_activate() {
        sut.start(mock(MessageBus.class));

        verify(serviceOne, times(1)).activate(any(ServiceContext.class));
        verify(serviceTwo, times(1)).activate(any(ServiceContext.class));
        verify(serviceThree, times(1)).activate(any(ServiceContext.class));
    }

    @Test
    public void start_activateWithMessage() {
        final MessageBus messages = mock(MessageBus.class);

        sut.start(messages);

        final ArgumentMatcher<ServiceContext> matcher = argument -> messages.equals(argument.getMessages());
        verify(serviceOne, times(1)).activate(argThat(matcher));
        verify(serviceTwo, times(1)).activate(argThat(matcher));
        verify(serviceThree, times(1)).activate(argThat(matcher));
    }

    @Test
    @Ignore
    public void start_autoStart() {
        sut.start(mock(MessageBus.class));

        verify(serviceThree, times(1)).start();
    }

    @Test
    public void stop() {
        sut.stop();

        verify(serviceThree, times(1)).stop();
    }

    @Test
    public void deactivate() {
        sut.deactivate();

        verify(serviceOne, times(1)).deactivate();
        verify(serviceTwo, times(1)).deactivate();
        verify(serviceThree, times(1)).deactivate();
    }

    @Test
    public void findService_forNull() {
        final Optional<Service> service = sut.findService(null);

        assertThat(service.isPresent(), is(false));
    }

    @Test
    @Ignore
    public void findService() {
    }

    interface ServiceOne extends Service {
    }

    interface ServiceTwo extends Service {
    }


    interface ServiceThree extends AutoStartingService {
    }

    static class DefaultServiceThree implements ServiceThree {
        @Require
        private ServiceOne one;
        @Require
        private ServiceTwo two;

        @Override
        public void start() {

        }
    }
}
package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.testing.DelayedRepeater;
import de.weltraumschaf.nano.api.service.AutoStartingService;
import de.weltraumschaf.nano.api.service.Require;
import de.weltraumschaf.nano.api.service.Service;
import de.weltraumschaf.nano.api.service.ServiceContext;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link Services}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class ServicesTest {
    private final ServiceOne serviceOne = mock(ServiceOne.class);
    private final ServiceTwo serviceTwo = mock(ServiceTwo.class);
    private final DefaultServiceThree serviceThree = new DefaultServiceThree();
    private final Services sut = new Services(Arrays.asList(
        serviceOne, serviceTwo, serviceThree));

    @Before
    public void letServiceTwoReturnHasStoppedTrue() {
        when(serviceTwo.hasStopped()).thenReturn(true);
    }

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
    }

    @Test
    public void start_activateWithMessage() {
        final MessageBus messages = mock(MessageBus.class);

        sut.start(messages);

        final ArgumentMatcher<ServiceContext> matcher =
            argument -> messages.equals(argument.getMessages());
        verify(serviceOne, times(1)).activate(argThat(matcher));
        verify(serviceTwo, times(1)).activate(argThat(matcher));
    }

    @Test // This test is flaky, dono why!
    public void start_autoStart() throws InterruptedException {
        sut.start(mock(MessageBus.class));

        DelayedRepeater.create(50, 3).execute(() -> {
            verify(serviceTwo, times(1)).start();
            return null;
        });
    }

    @Test
    public void stop_isRunning() {
        when(serviceTwo.isRunning()).thenReturn(true);

        sut.stop();

        verify(serviceTwo, times(1)).stop();
    }
    @Test
    public void stop_isNotRunning() {
        sut.stop();

        verify(serviceTwo, never()).stop();
    }

    @Test
    public void stop_deactivate() {
        sut.stop();

        verify(serviceOne, times(1)).deactivate();
        verify(serviceTwo, times(1)).deactivate();
    }

    @Test
    public void findService_forNull() {
        final Optional<Service> service = sut.findService(null);

        assertThat(service.isPresent(), is(false));
    }

    @Test
    public void findService_found() {
        final Optional<Service> service = sut.findService(ServiceOne.class);

        assertThat(service.isPresent(), is(true));
        assertThat(service.get(), is(serviceOne));
    }

    @Test
    public void findService_noFound() {
        final Optional<Service> service = sut.findService(String.class);

        assertThat(service.isPresent(), is(false));
    }

    interface ServiceOne extends Service {
    }

    interface ServiceTwo extends AutoStartingService {
    }


    interface ServiceThree extends Service {
    }

    private static class DefaultServiceThree implements ServiceThree {
        @Require
        private ServiceOne one;
        @Require
        private ServiceTwo two;
    }
}
package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.testing.DelayedRepeater;
import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.service.AutoStartingService;
import de.weltraumschaf.nano.api.service.Service;
import de.weltraumschaf.nano.api.service.ServiceContext;
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
 * Tests for {@link ServiceLifecycleManager}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class ServiceLifecycleManagerTest {
    private final ServiceOne serviceOne = mock(ServiceOne.class);
    private final ServiceTwo serviceTwo = mock(ServiceTwo.class);
    private final ServiceThree serviceThree = mock(ServiceThree.class);
    private final ServiceRegistry registry = new ServiceRegistry();
    private final ServiceLifecycleManager sut = new ServiceLifecycleManager(registry);

    @Before
    public void registerServices() {
        registry.register(serviceOne);
        registry.register(serviceTwo);
        registry.register(serviceThree);
    }

    @Before
    public void letServiceTwoReturnHasStoppedTrue() {
        when(serviceTwo.hasStopped()).thenReturn(true);
    }

    @Test(expected = NullPointerException.class)
    public void start_messagesNotNull() {
        sut.start(null);
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
    public void stop_deactivate() {
        sut.stop();

        verify(serviceOne, times(1)).deactivate();
        verify(serviceTwo, times(1)).deactivate();
    }

    interface ServiceOne extends Service {
    }

    interface ServiceTwo extends AutoStartingService {
    }


    interface ServiceThree extends Service {
    }

}
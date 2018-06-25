package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.messaging.MessageBus;
import de.weltraumschaf.nano.api.Require;
import de.weltraumschaf.nano.api.Service;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link Injector}.
 */
public class InjectorTest {
    private final DefaultRequiredServiceOne one = new DefaultRequiredServiceOne();
    private final DefaultRequiredServiceTwo two = new DefaultRequiredServiceTwo();
    private final DefaultServiceWhichRequires requires = new DefaultServiceWhichRequires();
    private MessageBus messages = mock(MessageBus.class);
    private final Injector sut = new Injector(new Services(Arrays.asList(
        one,
        two,
        requires
    )), messages);

    @Test(expected = NullPointerException.class)
    public void injectRequiredServices_notNull() {
        sut.injectRequired(null);
    }

    @Test
    public void injectRequired() {
        sut.injectRequired(requires);

        assertThat(requires.one, is(one));
        assertThat(requires.two, is(two));
        assertThat(requires.message, is(messages));
    }

    @Test
    public void findRequiredFields_null() {
        assertThat(sut.findRequiredFields(null), hasSize(0));
    }

    @Test
    public void findRequiredFields() {
        assertThat(sut.findRequiredFields(requires), hasSize(3));
    }

    interface ServiceWhichRequires extends Service {
    }

    private static class DefaultServiceWhichRequires implements ServiceWhichRequires {
        @Require
        private RequiredServiceOne one;
        @Require
        private RequiredServiceTwo two;
        private Object three;
        private Object four;
        @Require
        private MessageBus message;
    }

    interface RequiredServiceOne extends Service {
    }

    private static class DefaultRequiredServiceOne implements RequiredServiceOne {
    }

    interface RequiredServiceTwo extends Service {
    }

    private static class DefaultRequiredServiceTwo implements RequiredServiceTwo {
    }
}
